package toolchain.vm.cpu;

import toolchain.vm.VirtualMachine;

public class CPU {
	@SuppressWarnings("unused")
	private int ACC;
	private VirtualMachine vm;
	
	public CPU(VirtualMachine vm) {
		this.vm = vm;
	}
	
	public void pretendThisIsTheReadInstruction() {
		int input = vm.readInput();
		ACC = input; // or wherever it goes
	}
}
