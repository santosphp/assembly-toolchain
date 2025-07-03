package toolchain.vm;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import toolchain.vm.cpu.CPU;
import toolchain.vm.vmlistener.VMListener;

public class VirtualMachine {
	
	private final Queue<Integer> inputBuffer = new LinkedList<>();
	@SuppressWarnings("unused")
	private List<Integer> programData;
	private int mop;

	private VMListener listener;
	@SuppressWarnings("unused")
	private CPU cpu;
	private boolean isRunning;
	
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
		
		System.out.println(programData.getFirst());
		this.cpu.setMemory(new Memory(programData));
	}
	
	public void setListener(VMListener listener) {
	    this.listener = listener;
	}

	public void run() {
		while(isRunning) {
			isRunning = cpu.executeInstruction();
			// After each instruction:
			if (mop == 1 || mop == 2) {
			    if (listener != null) listener.onCycleCompleted();
			}
		}
		// When finished:
		if (mop == 1 || mop == 2) {
		    if (listener != null) listener.onProgramFinished();;
		}
	}

	public void setProgramData(List<Integer> programData) {
		// should probably reset the whole memory as well, like registers and inputBuffer
	    this.inputBuffer.clear();
		listener.onProgramDataInitialized();
		this.programData = programData;
	}

	public void step() {
		// Execute one instruction
		this.isRunning =  cpu.executeInstruction();
		if (mop == 1 || mop == 2) {
			listener.onCycleCompleted();
		}
	}
	
	public void printOutput(String message) {
		if (mop == 0)
            System.out.println(message);
		if (mop == 1 || mop == 2) {
			listener.onOutputProduced(message);
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
		// Should give a quick description of what the next instruction will do
		return "Test instruction";
	}

	public void setMop(int mop) {
		this.mop = mop;
	}

	public int getMop() {
		return mop;
	}
}
