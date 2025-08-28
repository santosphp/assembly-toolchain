package app.toolchain.loader;

import app.toolchain.vm.VirtualMachine;
import app.toolchain.Tables;
import app.toolchain.vm.Memory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Loader {
    private Tables tables;
    public Loader(Tables tables) {
        this.tables = tables;
    }

    public void load(VirtualMachine vm, String hpxOut, Integer stackSize) {
        if (vm == null || hpxOut == null) {
            throw new IllegalArgumentException("Parâmetros inválidos para Loader.");
        }

        List<Short> code = new ArrayList<>();
        int enderecoBase = 0;
        int relocMode = 0;

        // 1. Ler arquivo e extrair dados
        try (Scanner scanner = new Scanner(new File(hpxOut))) {
            if (!scanner.hasNextInt()) throw new IllegalArgumentException("Arquivo HPX inválido.");
            enderecoBase = scanner.nextInt();

            while (scanner.hasNextInt()) {
                code.add((short) scanner.nextInt());
            }

            if (code.isEmpty()) throw new IllegalArgumentException("Arquivo HPX sem código.");
            relocMode = code.remove(code.size() - 1); // Último valor = modo de relocação
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Arquivo HPX não encontrado: " + hpxOut, e);
        }

        Memory memory = vm.getCpu().getMemory(); // Use a memória já existente da VM
        memory.setStackMaxSize(stackSize);
        
        // Use o endereço base do segmento de código, nunca sobrescreva área reservada
        int enderecoEficaz = enderecoBase + stackSize;
        memory.setCodeSegmentBaseAddress(enderecoEficaz);

        // 2. Verificar se cabe na memória
        if (enderecoEficaz + code.size() > memory.getSize()) {
            throw new IllegalStateException("Programa não cabe na memória.");
        }

        // 3. Copiar código para memória
        for (int i = 0; i < code.size(); i++) {
            memory.write(enderecoEficaz + i, code.get(i));
        }

        // 4. Relocação dinâmica usando tabelas
        if (relocMode == 0) {
            // 4.1 Atualizar endereços na tabela de definição
            for (Map<String, Tables.definitionEntry> defTable : tables.getDefinitionTables()) {
                for (Map.Entry<String, Tables.definitionEntry> entry : defTable.entrySet()) {
                    Tables.definitionEntry def = entry.getValue();
                    int novoEndereco = def.endereco() + enderecoEficaz;
                    defTable.put(entry.getKey(), new Tables.definitionEntry(novoEndereco, def.modo()));
                }
            }

            // 4.2 Aplicar relocations
            if (tables.getFinalRelocationTable() != null) {
                for (Tables.relocationEntry reloc : tables.getFinalRelocationTable()) {
                    // Encontra o endereço real do símbolo
                    int enderecoSimbolo = -1;
                    for (Map<String, Tables.definitionEntry> defTable : tables.getDefinitionTables()) {
                        if (defTable.containsKey(reloc.symbolName())) {
                            enderecoSimbolo = defTable.get(reloc.symbolName()).endereco();
                            break;
                        }
                    }
                    if (enderecoSimbolo == -1) continue; // símbolo não encontrado

                    // Lê valor original da memória
                    int valorOriginal = memory.read(enderecoEficaz + reloc.offset());
                    // Atualiza valor (exemplo: soma endereço do símbolo)
                    memory.write(enderecoEficaz + reloc.offset(), valorOriginal + enderecoSimbolo);
                }
            }

            // 4.3 Marcar como relocacionado (simulado)
            relocMode = 1;
        }

        // 5. Carregar memória na VM
        vm.getCpu().setMemory(memory);
    }
}
