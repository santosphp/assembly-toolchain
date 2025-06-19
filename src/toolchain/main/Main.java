package memory;
import memory.Memoria;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		// Teste
        List<Integer> dados = new ArrayList<>();
        Memoria memoria = new Memoria(dados);

        System.out.println("=== Teste de PUSH ===");
        memoria.push(10);
        memoria.push(20);
        memoria.push(30);
        memoria.dumpPilha(); // Esperado: 10 20 30

        System.out.println("\n=== Teste de POP ===");
        System.out.println("Pop: " + memoria.pop()); // Esperado: 30
        memoria.dumpPilha();                         // Esperado: 10 20

        System.out.println("\n=== Teste de leitura e escrita ===");
        memoria.write(100, 999);
        System.out.println("Valor na posição 100: " + memoria.read(100)); // Esperado: 999

        System.out.println("\n=== Teste de proteção ao endereço base ===");
        memoria.write(2, 777); // Deve alertar que não pode escrever no endereco base

        System.out.println("\n=== Teste de underflow ===");
        memoria.pop(); // Remove 20
        memoria.pop(); // Remove 10
        memoria.pop(); // Pilha está vazia → Deve dar underflow

        System.out.println("\n=== Teste de overflow ===");
        for (int i = 1; i <= 70; i++) {
            memoria.push(i); // Limite é 64 → após 64 deve ocorrer overflow
        }

        memoria.dumpPilha(); // Mostra pilha atual até o limite

	}
}
