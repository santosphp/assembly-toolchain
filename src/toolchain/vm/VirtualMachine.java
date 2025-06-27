package toolchain.vm;

import java.util.List;

import toolchain.vm.vmlistener.VMListener;

public class VirtualMachine {

	private List<Integer> programData;
	private int mop;

	private VMListener listener;

	public void setListener(VMListener listener) {
	    this.listener = listener;
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
	    listener.onProgramDataInitialized();
		this.programData = programData;
	}

	public void step() {
		// Execute one instruction
		if (mop == 1 || mop == 2) {
			listener.onCycleCompleted();
		}
	}

	public String peekNextInstruction() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setMop(int mop) {
		this.mop = mop;
	}

	public int getMop() {
		return mop;
	}

}
