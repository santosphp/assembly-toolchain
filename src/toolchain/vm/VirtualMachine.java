package toolchain.vm;

import java.util.LinkedList;
import java.util.List;
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

	public void setListener(VMListener listener) {
	    this.listener = listener;
	}
	
	public VirtualMachine() {
		this.cpu = new CPU(this);
	}

	public void run() {
		// Not sure where to place this exactly, but I think you get the idea
		// After each instruction:
		if (mop == 1 || mop == 2) {
		    if (listener != null) listener.onCycleCompleted();
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
