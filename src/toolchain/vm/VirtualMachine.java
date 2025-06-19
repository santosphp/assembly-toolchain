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
		program.add(12); // Read
		program.add(100);
		
		program.add(12); // Read
		program.add(101);
		
		program.add(3); // LOAD
		program.add(100);
		
		program.add(2); // Add
		program.add(101);
		
		program.add(7); //Store
		program.add(102);
		
		program.add(8); //Write
		program.add(102);
		
		program.add(11); //Stop
		
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
