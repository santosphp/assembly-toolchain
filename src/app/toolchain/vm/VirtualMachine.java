package app.toolchain.vm;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

public class VirtualMachine {
	
	private final Queue<Integer> inputBuffer = new LinkedList<>();
	private Consumer<String> outputConsumer;
	@SuppressWarnings("unused")
	private List<Integer> programData;
	private int mop;

	@SuppressWarnings("unused")
	private CPU cpu;
	
	private boolean running;
	
	public VirtualMachine() {
		this.cpu = new CPU(this);
		this.programData = new ArrayList<>();
	}

	public void loadFromFile(String filePath) {
		try {
			System.out.println("Loading from file: " + filePath);
			File myObj = new File(filePath);
		    Scanner myReader = new Scanner(myObj);
		    while (myReader.hasNextLine()) {
		    	try {
			      int data = myReader.nextInt();
			      System.out.println(data);
			      programData.add(data);
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
		
		this.cpu.setMemory(new Memory(programData));
	}

	public void run() {
		//...
	}

	public void setProgramData(List<Integer> programData) {
		// Clean inputBuffer, registers and build new memory data
	    this.inputBuffer.clear();
		this.programData = programData;
		this.cpu.clearRegisters();
		this.cpu.setMemory(new Memory(programData));
	}

	public void step() {
		this.running = cpu.executeInstruction();
		//...
	}
	
	public void setOutputConsumer(Consumer<String> consumer) {
	    this.outputConsumer = consumer;
	}
	
	public void printOutput(String message) {
		if (outputConsumer != null) {
		    outputConsumer.accept(message);
		}
	}

	public void pushInput(int value) {
	    inputBuffer.add(value);
	}

	public int readInput() {
		if (inputBuffer.isEmpty()) {
	        // Pause execution, or throw an error
	        throw new IllegalStateException("Input buffer is empty!");
	    }
	    return inputBuffer.poll();
	}
	
	public String peekNextInstruction() {
		int nextOpcode = cpu.peekNextOpcode();
		
		switch (nextOpcode) {
		case 2: // ADD
			return ("ADD: Soma o valor do operando1 ao conteúdo do acumulador e armazena o resultado no próprio acumulador.");

		case 0: // BR
			return ("BR: Realiza um desvio incondicional para o endereço de memória especificado pelo operando1");

		case 5: // BRNEG
			return ("BRNEG: Se o valor no acumulador for negativo. Realiza um desvio condicional para o endereço de memória especificado pelo operando1");

		case 1: // BRPOS
			return ("BRPOS: Se o valor no acumulador for positivo. Realiza um desvio condicional para o endereço de memória especificado pelo operando1");

		case 4: // BRZERO
			return ("BRZERO: Se o valor no acumulador for zero. Realiza um desvio condicional para o endereço de memória especificado pelo operando1");

		case 15: // CALL
			return ("CALL: Chama uma sub-rotina desviando a execução do programa para o endereço de memória especificado pelo operando1");

		case 13: // COPY
			return ("COPY: Copia o valor do operando2 para a posição de memória indicada pelo operando1");

		case 10: // DIVIDE
			return ("DIVIDE: Divide o valor contido no acumulador pelo valor do operando1 e armazena o resultado no próprio acumulador.");

		case 3: // LOAD
			return ("LOAD: Carrega o valor do operando1 no acumulador.");

		case 14: // MULT
			return ("MULT: Multiplica o valor contido no acumulador pelo valor do operando1 e armazena o resultado no próprio acumulador.");

		case 17: // PUSH
			return ("PUSH: Empilha o valor contido no acumulador.");

		case 18: // POP
			return ("POP: Desempilha o valor contido no acumulador.");

		case 12: // READ
			return ("READ: Lê um valor da entrada e o armazena no endereço de memória indicado pelo operando1.");

		case 16: // RET
			return ("RET: Retorna de uma sub-rotina.");

		case 11: // STOP
			return ("STOP: Encerra a execução do programa.");

		case 7: // STORE
			return ("STORE: Armazena o conteúdo do acumulador na posição de memória indicada pelo operando1.");

		case 6: // SUB
			return ("SUB: Subtrai o valor do operando1 do conteúdo do acumulador e armazena o resultado no próprio acumulador.");

		case 8: // WRITE
			return ("WRITE: Escreve na saída o valor indicado pelo operando1.");

		default:
			return ("Opcode indefinido: " + nextOpcode);
		}
	}

	public void setMop(int mop) {
		this.mop = mop;
	}

	public int getMop() {
		return mop;
	}
	
	public Queue<Integer> getInputBuffer() {
		return this.inputBuffer;
	}

	public void notifyProgramFinished() {
		printOutput("Program finished!");
	}

	public boolean isHalted() {
		return this.running;
	}
}
