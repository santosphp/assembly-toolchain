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
		
		System.out.println(programData.getFirst());
		this.cpu.setMemory(new Memory(programData));
	}

	public void run() {
		this.running = true;
		
		while(this.running) {
			this.running = cpu.executeInstruction();
			//...
		}
	}

	public void setProgramData(List<Integer> programData) {
		// should probably reset the whole memory as well, like registers and inputBuffer
	    this.inputBuffer.clear();
		this.programData = programData;
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
		// Should give a quick description of what the next instruction will do
		return "Test instruction";
	}

	public void setMop(int mop) {
		this.mop = mop;
	}

	public int getMop() {
		return mop;
	}

	public void notifyProgramFinished() {
		printOutput("Program finished!");
	}

	public boolean isHalted() {
		return this.running;
	}
}
