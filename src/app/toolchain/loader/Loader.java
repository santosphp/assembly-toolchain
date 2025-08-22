package app.toolchain.loader;

import app.toolchain.vm.VirtualMachine;
import app.toolchain.vm.Memory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Loader {

    public void load(VirtualMachine vm, String hpxOut) {
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
        // Use o endereço base do segmento de código, nunca sobrescreva área reservada
        int enderecoEficaz = memory.getCodeSegmentBaseAddress();

        // 2. Verificar se cabe na memória
        if (enderecoEficaz + code.size() > memory.getSize()) {
            throw new IllegalStateException("Programa não cabe na memória.");
        }

        // 3. Copiar código para memória
        for (int i = 0; i < code.size(); i++) {
            memory.write(enderecoEficaz + i, code.get(i));
        }

        // 4. Relocação dinâmica (simples, pois não há tabela de símbolos/relocations reais)
        if (relocMode == 0) {
            // Aqui você faria a atualização de símbolos e relocations, se existissem.
            // Como não há, apenas marcamos como relocacionado (simulado).
            relocMode = 1;
        }

        // 5. Carregar memória na VM
        vm.getCpu().setMemory(memory);
    }
}
