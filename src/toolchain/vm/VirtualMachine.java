package toolchain.vm;

import java.util.List;
import java.util.ArrayList;

public class VirtualMachine {
	// Attributes
	private Cpu cpu;
	private List<Integer> programData;
	
	// Methods
	public List<Integer> loadFromFile(String filePath) {
		System.out.println("Loading from: " + filePath + "\n");
		List<Integer> program = new ArrayList<>();
		
		
		// Sumbtrai dois Imediatos 
		program.add(131); // LOAD Im
		program.add(10);
		
		program.add(134); // Sub Im
		program.add(8);
		
		program.add(7); //Store Dir
		program.add(102);
		
		program.add(8); //Write Dir
		program.add(102);
		
		program.add(11); //Stop

		// ----------------------------------------------
		/*
		// Soma 2 numeros endereçados indiretamente
		program.add(131); // LOAD Im
		program.add(10);

		program.add(7); // STORE Dir
		program.add(100);
		
		program.add(131); // LOAD Im
		program.add(100);
		
		program.add(7); // STORE Dir
		program.add(101);
		

		program.add(131); // LOAD Im
		program.add(20);

		program.add(7); // STORE Dir
		program.add(102);
		
		program.add(131); // LOAD Im
		program.add(102);
		
		program.add(7); // STORE Dir
		program.add(103);
		
		
		program.add(35); // LOAD In
		program.add(101);
		
		program.add(34); // Add In
		program.add(103);
		
		program.add(7); //Store
		program.add(104);
		
		program.add(8); //Write
		program.add(104);
		
		program.add(11); //Stop
		*/
		
		// ----------------------------------------------
		/*
		// Conta de 0 a 10 usando BR e BRZERO
		program.add(131); // LOAD Im
		program.add(0);

		program.add(7); // STORE Dir
		program.add(100);
		
		program.add(131); // LOAD Im
		program.add(10);

		program.add(7); // STORE Dir
		program.add(101);
		
		program.add(3); // LOAD Dir <---
		program.add(101);

		program.add(4); // BRZERO Dir
		program.add(24);

		program.add(134); // SUB Im
		program.add(1);

		program.add(7); // STORE Dir
		program.add(101);
		
		program.add(3); // LOAD Dir
		program.add(100);
		
		program.add(130); // ADD Im
		program.add(1);
		
		program.add(7); // STORE Dir
		program.add(100);

		program.add(0); //BR Dir
		program.add(8);
		
		program.add(8); //Write Dir
		program.add(100);
		
		program.add(11); //Stop
		*/
		
		return program;
	}
	
	public void startSimulation() {

		System.out.println("Starting execution...");
		
		while(cpu.executeInstruction()) {
			// Execute
		}
		
		System.out.println("Ending execution...");
	}
	
	// Constructor
	public VirtualMachine(int mop, String filePath) {
		super();
		this.programData = loadFromFile(filePath);
		this.cpu = new Cpu(mop, programData);
	}
	
	// Getters and Setters
	public Cpu getCpu() {
		return cpu;
	}

	public void setCpu(Cpu cpu) {
		this.cpu = cpu;
	}

	public List<Integer> getProgramData() {
		return programData;
	}

	public void setProgramData(List<Integer> programData) {
		this.programData = programData;
	}
}
