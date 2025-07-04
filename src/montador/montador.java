package montador;
import java.io.*;
import java.nio.file.*;
import java.util.*;


public class montador {
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
    }
    
    private final Map<String,Integer> symbolTable = new HashMap<>();
    private final Map<String,InstrDef>  instrSet    = new HashMap<>();
    private final List<SrcLine>         srcLines    = new ArrayList<>();

    private Path sourceFile;
    private String baseName;

    private BufferedWriter objW;
    private BufferedWriter lstW;
    private boolean errors = false;
    
    private void pass2() throws IOException {
        for (SrcLine sl : srcLines) {
            if (sl.raw.trim().isEmpty() || sl.raw.charAt(0) == '*') {
                lstW.write(String.format("%6s %8s %s%n", "", "", sl.raw));
                continue;
            }

            if (isDirective(sl.opcode)) {
                handleDirective(sl);
            } else {
                handleInstruction(sl);
            }
        }
    }

    private void handleInstruction(SrcLine sl) throws IOException {
        InstrDef def = instrSet.get(sl.opcode);
        if (def == null) {              
            lstW.write(String.format("%04X %-8s %s <-- Instrução inválida%n",
                                      sl.address, "", sl.raw));
            return;
        }

        int opcode = def.opcode;
        int mode   = 0;                  // 0=dir,1=imm,2=ind
        int operandVal = 0;

        if (sl.op1 != null) {
            String op = sl.op1;
            if (op.startsWith("#")) { mode = 1; operandVal = valueOf(op.substring(1)); }
            else if (op.endsWith(",I")) { mode = 2; operandVal = symbolValue(op.substring(0, op.length()-2), sl); }
            else { operandVal = symbolValue(op, sl); }
        }

        int word1 = (opcode << 4) | (mode & 0xF);
        String bytes = String.format("%02X%04X", word1, operandVal & 0xFFFF);

        writeObjText(sl.address, bytes);
        writeLstLine(sl.address, bytes, sl.raw);
    }

    private void handleDirective(SrcLine sl) throws IOException {
        switch (sl.opcode) {
            case "CONST" -> {
                String constHex = encodeConst(sl.op1);
                writeObjText(sl.address, constHex);
                writeLstLine(sl.address, constHex, sl.raw);
            }
            case "SPACE" -> {
                /* Reserva – apenas listagem */
                writeLstLine(sl.address, "", sl.raw);
            }
            case "START", "END", "STACK", "INTDEF", "INTUSE" -> {
                writeLstLine(sl.address, "", sl.raw);
            }
            default -> writeLstLine(sl.address, "", sl.raw);
        }
    }
    
    private void writeObjHeader() throws IOException { objW.write(String.format("H %-6s%n", baseName)); }
    private void writeObjText(int addr, String bytes) throws IOException { objW.write(String.format("T %04X %s%n", addr, bytes)); }
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
        if (trimmed.isEmpty() || trimmed.charAt(0) == '*') return null;   // comentário/vazio

        SrcLine sl = new SrcLine();
        sl.raw = line;

        // Tokenização básica por espaços
        String[] toks = trimmed.split("\\s+");
        int idx = 0;

        // label? (present se NÃO for opcode ou diretiva)
        if (!isOpcodeOrDir(toks[idx])) {
            sl.label = toks[idx++];
        }

        sl.opcode = toks[idx++].toUpperCase();
        if (idx < toks.length) sl.op1 = toks[idx++];
        if (idx < toks.length) sl.op2 = toks[idx];

        return sl;
    }

    private int valueOf(String token) {
        token = token.trim();
        if (token.startsWith("H'")) return Integer.parseInt(token.substring(2, token.length()-1), 16);
        if (token.startsWith("@"))  return Integer.parseInt(token.substring(1)); // literal decimal simplificado
        return Integer.parseInt(token);  // decimal
    }

    private int symbolValue(String sym, SrcLine src) {
        Integer v = symbolTable.get(sym);
        if (v == null) { markError("Símbolo não definido: " + sym + " (linha: " + src.raw + ")"); return 0; }
        return v;
    }

    private String encodeConst(String token) {
        if (token.startsWith("H'")) return token.substring(2, token.length()-1);
        int v = Integer.parseInt(token);
        return String.format("%04X", v);
    }

    private boolean isOpcodeOrDir(String t) { return instrSet.containsKey(t.toUpperCase()) || isDirective(t); }
    private boolean isDirective(String t) { return switch (t.toUpperCase()) {
        case "START","END","CONST","SPACE","STACK","INTDEF","INTUSE" -> true; default -> false; }; }

    private void loadInstrSet() {
        /*
         * Mapeamento dos mnemônicos para o opcode usado pela CPU
         * (ver classe toolchain.vm.Cpu – campo RI)
         * Tamanho padrão = 3 bytes para todas as instruções desta arquitetura
         */
        instrSet.put("BR",      new InstrDef(0x00, 3));
        instrSet.put("BRPOS",   new InstrDef(0x01, 3));
        instrSet.put("ADD",     new InstrDef(0x02, 3));
        instrSet.put("LOAD",    new InstrDef(0x03, 3));
        instrSet.put("BRZERO",  new InstrDef(0x04, 3));
        instrSet.put("BRNEG",   new InstrDef(0x05, 3));
        instrSet.put("SUB",     new InstrDef(0x06, 3));
        instrSet.put("STORE",   new InstrDef(0x07, 3));
        instrSet.put("WRITE",   new InstrDef(0x08, 3));
        instrSet.put("DIVIDE",  new InstrDef(0x0A, 3));
        instrSet.put("STOP",    new InstrDef(0x0B, 3));
        instrSet.put("READ",    new InstrDef(0x0C, 3));
        instrSet.put("COPY",    new InstrDef(0x0D, 3));
        instrSet.put("MULT",    new InstrDef(0x0E, 3));
        instrSet.put("CALL",    new InstrDef(0x0F, 3));
        instrSet.put("RET",     new InstrDef(0x10, 3));
        instrSet.put("PUSH",    new InstrDef(0x11, 3));
        instrSet.put("POP",     new InstrDef(0x12, 3));
    }

    private void openOutputs() throws IOException {
        objW = Files.newBufferedWriter(Paths.get(baseName + ".OBJ"));
        lstW = Files.newBufferedWriter(Paths.get(baseName + ".LST"));
    }
    private void closeOutputs() throws IOException { objW.close(); lstW.close(); }

    private void markError(String msg) { errors = true; System.err.println("ERRO: " + msg); }
    private String stripExt(String f) { int d = f.lastIndexOf('.'); return d>=0 ? f.substring(0,d) : f; }
}

	
}
