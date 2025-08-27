package app.toolchain.linker;

import app.toolchain.Tables;
import app.toolchain.Tables.definitionEntry;
import app.toolchain.Tables.relocationEntry;
import app.toolchain.Tables.useEntry;
import app.toolchain.Tables.Sinal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Linker {

    private record HeaderData(String baseName, Integer programSize, Integer programStack) {}

    private List<HeaderData> modulesHeaderData;
    private Map<String, definitionEntry> globalSymbolTable;
    private List<Short> codigoFinal;
    private Integer baseAddress;

    public Linker() {
        this.modulesHeaderData = new ArrayList<>();
        this.globalSymbolTable = new HashMap<>();
        this.baseAddress = 0;
    }

    public Integer link(List<String> modulos, Tables tables, boolean relocacaoFinal, int enderecoBase, String hpxOut) {
        if (modulos.isEmpty()) {
            System.out.println("No modules to link!");
            return 0;
        }

        // 1. Ler cabeçalhos e coletar informações
        for (String modulo : modulos) {
            if (!getHeaderInfo(modulo)) return 0;
        }
        
        Integer finalStackSize = 0;
        for (HeaderData header : this.modulesHeaderData) {
        	finalStackSize += header.programStack;
        }

        // 2. Construir Tabela de Símbolos Global
        int currentBaseAddress = 0;
        for (int i = 0; i < modulos.size(); i++) {
            Map<String, definitionEntry> definitionTable = tables.getDefinitionTables().get(i);
            for (Map.Entry<String, definitionEntry> symbolEntry : definitionTable.entrySet()) {
                String symbolName = symbolEntry.getKey();
                definitionEntry definition = symbolEntry.getValue();
                if (globalSymbolTable.containsKey(symbolName)) {
                    System.out.println("Duplicated symbol: " + symbolName);
                    return 0;
                }
                globalSymbolTable.put(symbolName, new definitionEntry(definition.endereco() + currentBaseAddress, definition.modo()));
            }
            currentBaseAddress += this.modulesHeaderData.get(i).programSize;
        }

        // 3. Verificar imports não resolvidos
        for (List<useEntry> useTable : tables.getUseTables()) {
            for (useEntry entry : useTable) {
                if (!globalSymbolTable.containsKey(entry.symbol())) {
                    System.out.println("Import not solved: " + entry.symbol());
                    return 0;
                }
            }
        }

        // 4. Construir código final
        int finalSize = this.modulesHeaderData.stream().mapToInt(HeaderData::programSize).sum();
        this.codigoFinal = new ArrayList<>(finalSize);

        currentBaseAddress = 0;
        for (int i = 0; i < modulos.size(); i++) {
            int moduleSize = this.modulesHeaderData.get(i).programSize;
            List<Short> moduleArray = getModuleArray(modulos.get(i), moduleSize);

            List<useEntry> useTable = tables.getUseTables().get(i);

            // Relocação local
            for (Integer offset : tables.getRelocationTables().get(i)) {
                short finalAddress = (short) (moduleArray.get(offset) + currentBaseAddress + finalStackSize);
                moduleArray.set(offset, finalAddress);
            }

            // Relocação externa
            if (relocacaoFinal) {
                for (useEntry entry : useTable) {
                    relocateInPlace(moduleArray, entry.symbol(), entry.lc(), entry.signal(), currentBaseAddress + finalStackSize);
                }
            } else {
                for (useEntry entry : useTable) {
                    int finalOffset = currentBaseAddress + finalStackSize + entry.lc();
                    tables.getFinalRelocationTable().add(new relocationEntry(entry.symbol(), finalOffset));
                }
            }

            // Copiar bytes para o código final
            this.codigoFinal.addAll(moduleArray);
            currentBaseAddress += moduleSize;
        }

        this.baseAddress = relocacaoFinal ? enderecoBase : 0;

        // 5. Salvar arquivo .HPX
        try {
            File hpxFile = new File(hpxOut);
            try (PrintWriter writer = new PrintWriter(hpxFile)) {
                writer.println(this.baseAddress); // Endereço inicial
                for (Short code : this.codigoFinal) {
                    writer.println(code);
                }
                writer.println(relocacaoFinal? 1 : 0); // Relocação final
            }
            
            System.out.println("Arquivo HPX salvo com sucesso: " + hpxFile.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("Erro ao salvar arquivo HPX: " + e.getMessage());
            return 0;
        }

        return finalStackSize;
    }

    public boolean getHeaderInfo(String modulo) {
        try {
            String filePath = modulo;
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);

            if (myReader.hasNextLine()) {
                String header = myReader.nextLine();
                if (header.matches("^H\\s+\\S+\\s+\\d+\\s+\\d+\\s*$")) {
                    Scanner lineScanner = new Scanner(header);
                    lineScanner.next();
                    String baseName = lineScanner.next().trim();
                    int programSize = lineScanner.nextInt();
                    int programStack = lineScanner.nextInt();
                    modulesHeaderData.add(new HeaderData(baseName, programSize, programStack));
                    lineScanner.close();
                } else {
                    System.out.println("Could not find header in module: " + modulo);
                    myReader.close();
                    return false;
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Module file not found: " + modulo);
            return false;
        }
        return true;
    }

    public List<Short> getModuleArray(String module, Integer size) {
        List<Short> moduleArray = new ArrayList<>(size);
        String filePath = module;

        try (Scanner myReader = new Scanner(new File(filePath))) {
            if (myReader.hasNextLine()) myReader.nextLine(); // Ignora header
            while (myReader.hasNext()) {
                if (myReader.hasNextInt()) {
                    moduleArray.add((short) myReader.nextInt());
                } else {
                    myReader.next();
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Module file not found: " + filePath);
        }

        return moduleArray;
    }

    public void relocateInPlace(List<Short> module, String symbolName, Integer addr, Sinal signal, int baseOffset) {
        int innerOffset = module.get(addr);
        int targetAddress = this.globalSymbolTable.get(symbolName).endereco() + baseOffset;
        if (signal == Sinal.NEGATIVO) {
            module.set(addr, (short) (targetAddress - innerOffset));
        } else {
            module.set(addr, (short) (targetAddress + innerOffset));
        }
    }

}
