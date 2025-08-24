package app.toolchain.assembler;

import app.toolchain.Tables;
import app.toolchain.Tables.definitionEntry;
import app.toolchain.Tables.useEntry;
import app.toolchain.Tables.ModoRelocabilidade;
import app.toolchain.Tables.Sinal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Assembler {
	private static class InstrDef {          // definição de instrução
        final int opcode;                    // opcode (8 bits)
        final int size;                      // tamanho em bytes
        InstrDef(int op, int sz) { opcode = op; size = sz; }
    }

    private static class SrcLine {           // linha armazenada após Passo 1
        int address;                         // endereço calculado
        String raw;                          // linha original
        String label;
        String opcode;
        String op1;
        String op2;
        
        public SrcLine(String raw) {
            this.raw = raw;
            this.label = "";
            this.opcode = "";
            this.op1 = "";
            this.op2 = "";
        }

        @Override
        public String toString() {
            return "SrcLine{" +
                   "address=" + address +
                   ", raw='" + raw + '\'' +
                   ", label='" + label + '\'' +
                   ", opcode='" + opcode + '\'' +
                   ", op1='" + op1 + '\'' +
                   ", op2='" + op2 + '\'' +
                   '}';
        }
    }
    
    private final Map<String,Integer> symbolTable;
    private final Map<String,InstrDef> instrSet;
    private final List<SrcLine> srcLines;
    
    private Map<String,definitionEntry> definitionTable;
	private Map<String,useEntry> useTable;
	private Set<String> knowExternalSymbols;

    private String baseName;
    private int programStack;
    
    private String macroFile;
    private String objPath;
    private String lstPath;

    private BufferedWriter objW;
    private BufferedWriter lstW;
    private int lc;
    private boolean errors = false;
    
    public Assembler() {
		this.srcLines = new ArrayList<>();
		this.symbolTable = new HashMap<>();
		this.instrSet = new HashMap<>(18);
        instrSet.put("BR",      new InstrDef( 0, 2));
        instrSet.put("BRPOS",   new InstrDef( 1, 2));
        instrSet.put("ADD",     new InstrDef( 2, 2));
        instrSet.put("LOAD",    new InstrDef( 3, 2));
        instrSet.put("BRZERO",  new InstrDef( 4, 2));
        instrSet.put("BRNEG",   new InstrDef( 5, 2));
        instrSet.put("SUB",     new InstrDef( 6, 2));
        instrSet.put("STORE",   new InstrDef( 7, 2));
        instrSet.put("WRITE",   new InstrDef( 8, 2));
        instrSet.put("DIVIDE",  new InstrDef(10, 2));
        instrSet.put("STOP",    new InstrDef(11, 1));
        instrSet.put("READ",    new InstrDef(12, 2));
        instrSet.put("COPY",    new InstrDef(13, 3));
        instrSet.put("MULT",    new InstrDef(14, 2));
        instrSet.put("CALL",    new InstrDef(15, 2));
        instrSet.put("RET",     new InstrDef(16, 1));
        instrSet.put("PUSH",    new InstrDef(17, 2));
        instrSet.put("POP",     new InstrDef(18, 2));
        
        this.definitionTable = new HashMap<>();
        this.useTable = new HashMap<>();
        this.knowExternalSymbols = new HashSet<>();
        
        this.lc = 0;
    }

    public boolean assemble(String macroFile, String objPath, String lstPath, Tables tables) throws IOException {
    	// To check errors after every step...
    	// If a method returns true, an error occurred.
    	this.macroFile = macroFile;
    	this.objPath = objPath;
    	this.lstPath = lstPath;
    	
    	this.definitionTable.clear();
        this.useTable.clear();
        this.knowExternalSymbols.clear();
    	
    	boolean error;

		this.openOutputs();
    	
        error = readInputFile(this.macroFile);
        if (error)
        {
    		this.writeLstFooter();
    		this.closeOutputs();
        	return false;
        }
        
        error = step1();
        if (error)
        {
    		this.writeLstFooter();
    		this.closeOutputs();
        	return false;
        }
    	
		this.writeObjHeader();
		error = step2();
		this.writeObjEnd();
		this.writeLstFooter();
		this.closeOutputs();
        if (error) { return false; }
        
        tables.getDefinitionTables().add(new HashMap<>(this.definitionTable));
        tables.getUseTables().add(new HashMap<>(this.useTable));
        
        return true;
    }
    
    private boolean step1() throws IOException {
    	boolean hasEndDirective = false;
    	
        for (SrcLine sl : srcLines) {
        	// Ignore comments
            if (sl.raw.trim().isEmpty() || sl.raw.charAt(0) == '*') {
                continue;
            }

            // Its a directive
            if (isDirective(sl.opcode)) {
            	
            	// Invalid operands here too
            	
                // Directive switch
            	switch (sl.opcode) {
	            	case "START":
	            		this.baseName = sl.op1;
	            		break;
	            	case "END":
	            		hasEndDirective = true;
	            		break;
	            	case "INTDEF":
	            		if(!sl.op1.isBlank())
	            		{
	            			if(this.definitionTable.get(sl.op1.toUpperCase()) != null)
	            			{
	            				markError("Simbolo redefinido: Referência simbólica com definições múltiplas.");
	            				return true;
	            			}
	            			else
	            			{
	            				this.definitionTable.put(sl.op1.toUpperCase(), null);
	            			}
	            		}
	            		break;
	            	case "INTUSE":
	            		if(!sl.label.isBlank())
	            		{
	            			if(this.useTable.get(sl.label.toUpperCase()) != null)
	            			{
	            				markError("Simbolo redefinido: Referência simbólica com definições múltiplas.");
	            				return true;
	            			}
	            			else
	            			{
	            				this.knowExternalSymbols.add(sl.label.toUpperCase());
	            				//this.useTable.put(sl.op1, new useEntry(this.lc, ModoRelocabilidade.ABSOLUTO, true));
	            			}
	            		}
	            		break;
	            	case "CONST","SPACE":
	            		if(this.definitionTable.get(sl.label.toUpperCase()) != null)
	            		{
            				this.definitionTable.replace(sl.label.toUpperCase(), new definitionEntry(this.lc, ModoRelocabilidade.ABSOLUTO));
	            		}
	            		
	            		this.symbolTable.put(sl.label.toUpperCase(), this.lc);
	            		this.lc += 1;
            			break;
	            	case "STACK":
	            		this.programStack = Integer.parseInt(sl.op1);
	            		break;
            	}
            } 
            else // Its a instruction
            {
            	InstrDef def = instrSet.get(sl.opcode);

            	System.out.println(lc + " " + sl.opcode + " " + sl.op1 + " " + sl.op2 + " " + def.size);
            	
            	// Syntax ERROR
            	if (identifyOperendsError(sl, def)) {
    				markError("Erro de sintaxe: Falta ou excesso de operandos em instruções, ou labels mal formados.");
    				return true;
            	}
            	
            	if (def != null)
            	{
            		// Add label to symbol table
            		if (sl.label != null && !sl.label.isBlank()) {
            			// Redefinition ERROR
            			if (symbolTable.containsKey(sl.label)) {
            				markError("Simbolo redefinido: Referência simbólica com definições múltiplas.");
            				return true;
            			}
            			
            			symbolTable.put(sl.label.toUpperCase(), this.lc);
            		}
            		this.lc += def.size;
            	}
            	else
            	{
            		// Invalid instruction ERROR
            	    markError("Instrução inválida: O mnemônico {" + sl.opcode + "} não corresponde a nenhuma instrução do computador.");
            	    return true;
            	}
            }
        }
        if(!hasEndDirective) {
    		// End directive missing ERROR
    	    markError("Falta diretiva END: Indicação da ausência de pseudo-instrução END.");
    	    return true;
        }
        return false;
    }
    
    private boolean step2() throws IOException {
    	this.lc = 0;
    	
        for (SrcLine sl : srcLines) {
            if (sl.raw.trim().isEmpty() || sl.raw.charAt(0) == '*') {
                lstW.write(String.format("%6s %8s %s%n", "", "", sl.raw));
                continue;
            }
        	InstrDef def = instrSet.get(sl.opcode);

            if (isDirective(sl.opcode)) {
                if(handleDirective(sl)) { return true; }
            } else {
                if(handleInstruction(sl)) { this.lc += def.size; return true; }
            }
        }
        return false;
    }
    
    private boolean readInputFile(String macroFile) {
    	boolean parseError = false;
    	String filePath = macroFile;
		try {
			System.out.println("Loading from file: " + filePath);
			File myObj = new File(filePath);
		    Scanner myReader = new Scanner(myObj);
		    while (myReader.hasNextLine()) {
		    	try {
					String data = myReader.nextLine();
					System.out.println(data);
					SrcLine parsed = parseLine(data);
			        if (parsed != null) {
			            srcLines.add(parsed);
			        }else {
			        	parseError = true;
			        }
		    	} catch (Error e) {
					 e.printStackTrace();
					 continue;
		    	}
		    }
		    myReader.close();
		} catch (FileNotFoundException e) {
		    System.out.println("An error occurred fetching the instructions.");
		    e.printStackTrace();
		}
		return parseError;
    }

    private boolean handleInstruction(SrcLine sl) throws IOException {
        InstrDef def = instrSet.get(sl.opcode);
        if (def == null) {              
            lstW.write(String.format("%04X %-8s %s <-- Instrução inválida%n",
                                      sl.address, "", sl.raw));
            return false;
        }

        int opcode = def.opcode;
        int mode   = 0;
        Integer operandVal1 = 0;
        Integer operandVal2 = 0;

        if (!sl.op1.isBlank()) {
            String op1 = sl.op1;
            if (op1.startsWith("#")) { mode = 128; operandVal1 = valueOf(op1.substring(1)); }
            else if (op1.endsWith(",I")) { mode = 32; operandVal1 = symbolValue(op1.substring(0, op1.length()-2), sl); }
            else { operandVal1 = symbolValue(op1, sl); }
        }
        // Symbol not defined ERROR (error already marked, just interrupting execution)
        if(operandVal1 == null) { return true; }
        
        // Value out of bounds ERROR
        if(operandVal1 > 0xFFFF) {
        	markError("Valor fora dos limites: Operando1 muito longo para o tamanho de palavra do computador.");
        	return true;
        }
        
        // | addr mode    | opcode                 |
        // | b7 | b6 | b5 | b4 | b3 | b2 | b1 | b0 |
        int word = mode | (opcode & 0x1F);
        
        if (!sl.op2.isBlank()) {
            String op2 = sl.op2;
            if (op2.startsWith("#")) { mode = 128; operandVal2 = valueOf(op2.substring(1)); }
            else if (op2.endsWith(",I")) { mode = 64; operandVal2 = symbolValue(op2.substring(0, op2.length()-2), sl); }
            else { operandVal2 = symbolValue(op2, sl); }
        }
        // Symbol not defined ERROR (error already marked, just interrupting execution)
        if(operandVal2 == null) { return true; }
        
        // Value out of bounds ERROR
        if(operandVal2 > 0xFFFF) {
        	markError("Valor fora dos limites: Operando2 muito longo para o tamanho de palavra do computador.");
        	return true;
        }

        word = mode | word;
        
        //String word1 = to16BitString(word);
        //String word2 = to16BitString(operandVal1);
        //String word3 = to16BitString(operandVal2);
        
        //String bytes = String.format("%s %s %s", word & 0xFFFF, operandVal1, operandVal2 & 0xFFFF);
        String bytes = Integer.toString(word);
        if(operandVal1 != 0) {
            bytes = bytes.concat(String.format(" %d", operandVal1));
        }
        if(operandVal2 != 0) {
        	bytes = bytes.concat(String.format(" %d", operandVal2));
        }

        writeObjText(bytes);
        writeLstLine(sl.address, bytes, sl.raw);
        return false;
    }
    
    /*
    private static String to16BitString(int number) {
    	
        String binary = Integer.toBinaryString(number);

        if (binary.length() > 16) {
            binary = binary.substring(binary.length() - 16);
        }
        return String.format("%16s", binary).replace(' ', '0');
    }
	*/

    private boolean handleDirective(SrcLine sl) throws IOException {
        switch (sl.opcode) {
            case "CONST" -> {
                String constStr = encodeConst(sl.op1);
                Integer constInt = Integer.parseInt(constStr);
                // Value out of bounds ERROR
                if(constInt > 0xFFFF) {
                	markError("Valor fora dos limites: Constante muito longa para o tamanho de palavra do computador.");
                	return true;
                }
                writeObjText(constStr);
                writeLstLine(sl.address, constStr, sl.raw);
            }
            case "SPACE" -> {
                /* Reserva – apenas listagem */
                writeObjText("0");
                writeLstLine(sl.address, "", sl.raw);
            }
            case "START", "END", "STACK", "INTDEF", "INTUSE" -> {
                writeLstLine(sl.address, "", sl.raw);
            }
            default -> writeLstLine(sl.address, "", sl.raw);
        }
        return false;
    }
    
    private void writeObjHeader() throws IOException { objW.write(String.format("H %-6s %-6d%n", baseName, programStack)); }
    private void writeObjText(String bytes) throws IOException { objW.write(String.format("%s%n", bytes)); }
    private void writeObjEnd() throws IOException { objW.write("E\n"); }

    private void writeLstLine(int addr, String code, String src) throws IOException {
        lstW.write(String.format("%04X %-8s %s%n", addr, code, src));
    }

    private void writeLstFooter() throws IOException {
        lstW.write("\n");
        lstW.write(errors ? "Montagem encerrada COM ERROS\n" : "Nenhum erro detectado\n");
    }
    
    private SrcLine parseLine(String line) {
		String trimmed = line.trim();

		if (trimmed.length() > 80) {
			markError("Linha muito longa: Não deve haver mais de 80 caracteres numa linha.");
			return null;
		}

		//if (!trimmed.matches("^[a-zA-Z0-9 ,#@_\\s]*(\\*.*)?$")) {
		if (!trimmed.matches("^[a-zA-Z0-9 ,#@&_\\s]*(\\*.*)?$")) {
			markError(
					"Caracter inválido: Unidade sintática não reconhecida (caracter inválido em algum elemento da linha).");
			return null;
		}

		SrcLine sl = new SrcLine(line);
		sl.label = "";
		sl.opcode = "";
		sl.op1 = "";
		sl.op2 = "";

		if (!trimmed.isEmpty()) {
			String linhaSemComentario = trimmed;
			int commentIndex = linhaSemComentario.indexOf('*');

			if (commentIndex != -1) {
				linhaSemComentario = linhaSemComentario.substring(0, commentIndex);
			}

			linhaSemComentario = linhaSemComentario.trim();

			if (!linhaSemComentario.isEmpty()) {

				String[] toks = linhaSemComentario.split("\\s+");
				int idx = 0;

				if (!isOpcodeOrDir(toks[idx])) {
					sl.label = toks[idx++];
				}

				if (idx < toks.length) {
					sl.opcode = toks[idx++].toUpperCase();
				}
				if (idx < toks.length) {
					sl.op1 = toks[idx++];
				}
				if (idx < toks.length) {
					sl.op2 = toks[idx];
				}
			}
		}

		return sl;
	}

    /*
    private int valueOf(String token) {
        token = token.trim();
        if (token.startsWith("H'")) return Integer.parseInt(token.substring(2, token.length()-1), 16);
        if (token.startsWith("@"))  return Integer.parseInt(token.substring(1)); // literal decimal simplificado
        return Integer.parseInt(token);  // decimal
    }
    */
    
    private int valueOf(String token) {
        token = token.trim();
        try {
            if (token.startsWith("H'") && token.endsWith("'")) {
                String hex = token.substring(2, token.length() - 1);
                if (!hex.matches("[0-9A-Fa-f]+")) {
                    markError("Dígito inválido em número hexadecimal: " + token);
                    return 0;
                }
                return Integer.parseInt(hex, 16);
            } else if (token.startsWith("@")) {
                String lit = token.substring(1);
                if (!lit.matches("\\d+")) {
                    markError("Dígito inválido em literal: " + token);
                    return 0;
                }
                return Integer.parseInt(lit);
            } else {
                if (!token.matches("\\d+")) {
                    markError("Dígito inválido em número decimal: " + token);
                    return 0;
                }
                return Integer.parseInt(token);
            }
        } catch (NumberFormatException e) {
            markError("Erro ao interpretar número: " + token);
            return 0;
        }
    }

    private Integer symbolValue(String sym, SrcLine src) {
    	Integer v = symbolTable.get(sym);
        // If is not in symbol table
        if(v == null)
        {
	    	
	    	// useTable segment
			String symbolName = sym;
		    int offset = 0;
		    Sinal operator = Sinal.SOMA;
	    	
	    	int plusPos = sym.lastIndexOf('+');
	        int minusPos = sym.lastIndexOf('-');
	        
	    	int operatorPos = Math.max(plusPos, minusPos);
	
	    	// If the Symbol has a signal
	        if (operatorPos > 0) {
	            // Split the symbol and signal
	        	symbolName = sym.substring(0, operatorPos).trim();
	
	        	// Is not a known external symbol
	        	if(!this.knowExternalSymbols.contains(sym))
	        	{
	        		markError("Símbolo não definido: " + sym + " (linha: " + src.raw + ")");
	        		return null;
	        	}
	            
	            if(plusPos != -1) {
	                operator = Sinal.SOMA;
	            }else {
	                operator = Sinal.SUBTRACAO;
	            }
	            String numberStr = sym.substring(operatorPos + 1).trim();
	
	            try {
	                offset = Integer.parseInt(numberStr);
	                if (operator == Sinal.SUBTRACAO) {
	                    offset = -offset;
	                }
	
	            	this.useTable.put(symbolName, new useEntry(this.lc +1, ModoRelocabilidade.ABSOLUTO, operator));
	            	// Write the offset in object code for linker
	            	v = offset;
	
	            } catch (NumberFormatException e) {
	        		markError("Deslocamento não reconhecido: " + numberStr + " (linha: " + src.raw + ")");
	        		return null;
	            }
	
	        } else {
	        	// Is not a known external symbol
	        	if(!this.knowExternalSymbols.contains(sym))
	        	{
	        		markError("Símbolo não definido: " + sym + " (linha: " + src.raw + ")");
	        		return null;
	        	}
	        	
	        	this.useTable.put(sym, new useEntry(this.lc +1, ModoRelocabilidade.ABSOLUTO, Sinal.SOMA));
	        	// offset is 0 by default
	        	v = offset;
	        }
        }

        return v;
    }

    private String encodeConst(String token) {
        if (token.startsWith("H'")) return token.substring(2, token.length()-1);
        int v = Integer.parseInt(token);
        return String.format("%d", v);
    }

    private boolean isOpcodeOrDir(String t) { return instrSet.containsKey(t.toUpperCase()) || isDirective(t); }
    private boolean isDirective(String t) { return switch (t.toUpperCase()) {
        case "START","END","CONST","SPACE","STACK","INTDEF","INTUSE" -> true; default -> false; }; }

    private void openOutputs() throws IOException {
	    objW = Files.newBufferedWriter(Paths.get(objPath));
	    lstW = Files.newBufferedWriter(Paths.get(lstPath));
    }
    private void closeOutputs() throws IOException { objW.close(); lstW.close(); }

    private void markError(String msg) { errors = true; System.err.println("ERRO: " + msg); }
    private String stripExt(String f) { int d = f.lastIndexOf('.'); return d>=0 ? f.substring(0,d) : f; }
    
    private boolean identifyOperendsError(SrcLine sl, InstrDef def) {
    	int operands = 0;
    	if (!sl.op1.isBlank()) {operands += 1;}
    	if (!sl.op2.isBlank()) {operands += 1;}
    	if(def.size-1 == operands) {return false;}
    	return true;
    }
}
