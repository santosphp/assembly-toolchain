package toolchain.vm;

import java.util.List;
import java.util.ArrayList;

public class Instruction {
	// Attributes
	private int opcode;
	private List<Integer> operands;
	private int addrMode;
	private int size;

	// Constructor
	public Instruction() {
		super();
		this.opcode = -1;
		this.operands = new ArrayList<>(2);
		this.addrMode = -1;
		this.size = -1;
	}

	// Getters and Setters
	public int getOpcode() {
		return opcode;
	}

	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}

	public List<Integer> getOperands() {
		return operands;
	}

	public void setOperands(List<Integer> operands) {
		this.operands = operands;
	}

	public int getAddrMode() {
		return addrMode;
	}

	public void setAddrMode(int addrMode) {
		this.addrMode = addrMode;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
