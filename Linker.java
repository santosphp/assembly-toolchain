package app.toolchain.linker;

import app.toolchain.Tables;
import app.toolchain.Tables.definitionEntry;
import app.toolchain.Tables.relocationEntry;
import app.toolchain.Tables.useEntry;
import app.toolchain.Tables.Sinal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class Linker {
	
	private record HeaderData (String baseName, Integer programSize, Integer programStack) {}
	
	private List<HeaderData> modulesHeaderData;
	private Map<String, definitionEntry> globalSymbolTable;
	private List<Short> codigoFinal;
	private Integer baseAddress;
	
	public Linker()
	{
		this.modulesHeaderData = new ArrayList<>();
		this.globalSymbolTable = new HashMap<>();
		this.baseAddress = 0;
	}

	public boolean link(List<String> modulos, Tables tables, boolean relocacaoFinal, int enderecoBase, String hpxOut) {
		/*
		 Se modulos estiver vazio:
			 Erro "Nenhum módulo para linkar"
		*/
		if(modulos.isEmpty())
		{
			System.out.println("No modules to link!");
			return false;
		}
		
		/*
		// 1. Definir Bases e Símbolos Globais
		Para cada módulo:
			Definir onde ele começa (base offset)
			Adicionar símbolos globais (verificar duplicados)
		*/
		
		for(String modulo : modulos)
		{
			System.out.println("Trying to load:" + modulo);

			if(!getHeaderInfo(modulo)) { return false; }
		}

		Integer currentBaseAddress = 0;
		for(int i=0; i<modulos.size(); i++)
		{  
			Map<String, definitionEntry> definitionTable = tables.getDefinitionTables().get(i);
			
            for (Map.Entry<String, definitionEntry> symbolEntry : definitionTable.entrySet())
            {
                String symbolName = symbolEntry.getKey();
                definitionEntry definition = symbolEntry.getValue();

                if (globalSymbolTable.containsKey(symbolName))
                {
    	    		System.out.println("Duplicated symbol!");
    	    		return false;
                }
                else
                {
                    globalSymbolTable.put(symbolName, new definitionEntry(definition.endereco() + currentBaseAddress, definition.modo()));
                }
            }
			currentBaseAddress += this.modulesHeaderData.get(i).programSize;
		}
		
		/*
		// 2. Verificar se todos os imports são resolvidos
		Para cada módulo:
			Para cada símbolo importado:
				Se símbolo não encontrado nos globais:
					Erro "Import não resolvido"
		*/
		for(List<useEntry> useTable : tables.getUseTables())
		{  
			for (useEntry entry : useTable)
			{
				if (!globalSymbolTable.containsKey(entry.symbol()))
                {
    	    		System.out.println("Import not solved!");
    	    		return false;
                }
			}
		}
		
		/*
		// 3. Construir Código Final + Tabela de Símbolos + Relocações
		Inicializar array CodigoFinal com tamanho total dos módulos
		Para cada módulo:
			Se relocacaoFinal for verdadeira:
				Para cada registro de relocação:
					Chamar relocateInPlace (corrige endereços diretamente no código)
			Senão:
				Guardar relocations para o Loader resolver depois
			Copiar bytes do módulo para o CodigoFinal
			Ajustar símbolos para refletirem a posição real no CodigoFinal
		*/
		Integer finalSize = 0;
		for(HeaderData data : this.modulesHeaderData)
		{
			finalSize += data.programSize;
		}
		this.codigoFinal = new ArrayList<>(finalSize);

		currentBaseAddress = 0;
		for(int i=0; i<modulos.size(); i++)
		{	
			int moduleSize = this.modulesHeaderData.get(i).programSize;
			List<Short> moduleArray = getModuleArray(modulos.get(i), moduleSize);
			
			List<useEntry> useTable = tables.getUseTables().get(i);
			
			if(relocacaoFinal)
			{
				for (useEntry entry : useTable)
				{
	                relocateInPlace(moduleArray, entry.symbol(), entry.lc(), entry.signal(), currentBaseAddress);
				}
			}
			else
			{
				// Guardar relocations para o Loader resolver depois
				for (useEntry entry : useTable)
				{
					// Calcula o endereço absoluto no código final
	                int finalOffset = currentBaseAddress + entry.lc();
	                
	                // Adiciona o registro à tabela de relocação final
	                tables.getFinalRelocationTable().add(new relocationEntry(entry.symbol(), finalOffset));
				}
			}

			// Copiar bytes do módulo para o CodigoFinal
			for(Short value : moduleArray)
			{
				this.codigoFinal.add(value);
			}
			
			// Ajustar símbolos para refletirem a posição real no CodigoFinal
			/* Já foi ajustado nesse ponto */
			
			// Atualiza o endereço base para o próximo modulo
			currentBaseAddress += moduleSize;
		}
		
		/*
		// 4. Determinar Endereço de Início
 		Se relocacaoFinal for verdadeira:
 			EnderecoInicio = enderecoBase
 		Senão:
 			EnderecoInicio = endereço inicial do primeiro módulo
		 */
		this.baseAddress = relocacaoFinal ? enderecoBase : 0; // 0 ou o tamanho da pilha?
		
		
		/*
		// 5. Combinar Fontes (opcional para debug)
		Juntar os fontes dos módulos para futura depuração
		*/
		StringBuilder combinedSources = new StringBuilder();
		for (String modulo : modulos) {
			try (Scanner reader = new Scanner(new File(modulo))) {
				while (reader.hasNextLine()) {
					combinedSources.append(reader.nextLine()).append("\n");
				}
			} catch (FileNotFoundException e) {
				System.out.println("Could not read source for debug combination: " + modulo);
			}
		}

		/*
		// 6. Criar Objeto Final
		Criar um arquivo objeto com:
		- EnderecoInicio
		- CodigoFinal
		- Tabela de Símbolos ajustada
		- Registros de relocação (se relocacaoFinal = falso)
		*/
		StringBuilder objetoFinal = new StringBuilder();

		objetoFinal.append(this.baseAddress).append("\n");

		for (int codigo : codigoFinal) {
		    objetoFinal.append(codigo).append("\n");
		}

		objetoFinal.append(relocacaoFinal ? 1 : 0).append("\n");

		/*
		// 7. Salvar arquivos (.obj e .meta)
		Escrever arquivo .obj (formato textual)
		Escrever arquivo .meta (formato binário)
		*/
		try {
			// Salva .obj como texto
			File objFile = new File(hpxOut + ".obj");
			java.io.FileWriter writerObj = new java.io.FileWriter(objFile);
			writerObj.write(objetoFinal.toString());
			writerObj.close();

			// Salva .meta como binário
			File metaFile = new File(hpxOut + ".meta");
			try (java.io.FileOutputStream fos = new java.io.FileOutputStream(metaFile)) {
				for (Short code : this.codigoFinal) {
					fos.write(code);
				}
			}

			System.out.println("Arquivos salvos com sucesso: " + hpxOut + ".obj e " + hpxOut + ".meta");
		} catch (Exception e) {
			System.out.println("Erro ao salvar arquivos finais: " + e.getMessage());
		}
		
		return true;
	}
	
	public boolean getHeaderInfo(String modulo)
	{
		try {
			System.out.println("Loading from file: " + modulo);
			File myObj = new File(modulo);
		    Scanner myReader = new Scanner(myObj);
		    
		    // Pegar o cabeçalho
		    if(myReader.hasNextLine())
		    {
		    	String header = myReader.nextLine();
		    	
		    	if(header.matches("^H\\s+\\S+\\s+\\d+\\s+\\d+$"))
		    	{
		    		Scanner lineScanner = new Scanner(header);

	                try {
	                    // Ignora H
	                    lineScanner.next();

	                    // Ler os tokens em ordem
	                    String baseName = lineScanner.next();
	                    int programSize = lineScanner.nextInt();
	                    int programStack = lineScanner.nextInt();
	                    
	                    // Salvas o cabeçalho desse módulo
	                    modulesHeaderData.add(new HeaderData(baseName, programSize, programStack));

	                } catch (Exception e) {
	                    System.out.println("Error extracting header data: " + e.getMessage());
	                } finally {
	                    lineScanner.close();
	                }
		    	}
		    	else
		    	{
		    		System.out.println("Could not find " + modulo + "header.");
				    myReader.close();
				    return false;
		    	}
		    }
		    
		    myReader.close();
		} catch (FileNotFoundException e) {
		    System.out.println("An error occurred fetching the instructions.");
		    e.printStackTrace();
		}
		return true;
	}
	
	public List<Short> getModuleArray(String module, Integer size)
	{
		List<Short> moduleArray = new ArrayList<>(size);
		
		System.out.println("Trying to load from:" + module);

		try {
			System.out.println("Loading from file: " + module);
			File myObj = new File(module);
		    Scanner myReader = new Scanner(myObj);

	    	// Ignores the header
		    if(myReader.hasNextLine())
		    {
		    	myReader.nextLine();
		    }
		    
		    while (myReader.hasNext()) {
		        try {
		            if (myReader.hasNextInt()) {
		                int data = myReader.nextInt();
		                // System.out.println(data);
		                moduleArray.add((short) data);
		            } else {
		                myReader.next();
		            }
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		    myReader.close();
		  } catch (FileNotFoundException e) {
		    System.out.println("An error occurred fetching the instructions.");
		    e.printStackTrace();
		 }
		
		return moduleArray;
	}
	
	public void relocateInPlace(List<Short> module, String symbolName, Integer addr, Sinal signal, int baseOffset)
	{
		// corrige endereços diretamente no código
		int innerOffset = module.get(addr);
		
		if(signal == Sinal.SUBTRACAO)
		{
			module.set(addr, (short) (this.globalSymbolTable.get(symbolName).endereco() - innerOffset));
		}
		else
		{
			module.set(addr, (short) (this.globalSymbolTable.get(symbolName).endereco() + innerOffset));
		}
	}

}
