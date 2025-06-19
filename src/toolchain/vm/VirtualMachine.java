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
		program.add(100);
		program.add(200);
		program.add(50);
		program.add(-1);
		return program;
	}
	
	public void startSimulation() {
		
		while(cpu.executeInstruction()) {
			// Execute
		}
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
