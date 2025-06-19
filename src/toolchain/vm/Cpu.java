package toolchain.vm;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Cpu {
	// Attributes
	private Register pc;
	private Register sp;
	private Register acc;
	private Register ri;
	private Register re;
	private Register ro;
	private Register mop;
	private Memory memory;
	private Boolean running;
	
	// Methods
	public void executeInstruction(Instruction currentInstruction) {
		
	}
	
	public void setMop(Integer mop) {
		this.mop.setValue(mop & 0xFF);
	}
	
	public Map<String, Integer> getRegistersState() {
		Map<String, Integer> registersState = new HashMap<>();
		registersState.put(this.pc.getId(), this.pc.getValue());
		registersState.put(this.sp.getId(), this.sp.getValue());
		registersState.put(this.acc.getId(), this.acc.getValue());
		registersState.put(this.ri.getId(), this.ri.getValue());
		registersState.put(this.re.getId(), this.re.getValue());
		registersState.put(this.ro.getId(), this.ro.getValue());
		registersState.put(this.mop.getId(), this.mop.getValue());
		return registersState;
	}
	
	public Integer decodeAdressingMode(Integer opcode) {
		// Implementation
		return 0;
	}
	
	// Constructor
	public Cpu(int mopValue, List<Integer> programData) {
		super();
		this.pc = new Register(128, 16, "PC");
		this.sp = new Register(1, 16, "SP");
		this.acc = new Register(0, 16, "ACC");
		this.ri = new Register(0, 16, "RI");
		this.re = new Register(0, 16, "RE");
		this.ro = new Register(0, 16, "RO");
		this.mop = new Register(mopValue, 16, "MOP");
		this.memory = new Memory(16, 2048, programData);
		this.running = true;
	}

	// Getters and Setters
	public Register getPc() {
		return pc;
	}

	public void setPc(Register pc) {
		this.pc = pc;
	}

	public Register getSp() {
		return sp;
	}

	public void setSp(Register sp) {
		this.sp = sp;
	}

	public Register getAcc() {
		return acc;
	}

	public void setAcc(Register acc) {
		this.acc = acc;
	}

	public Register getRi() {
		return ri;
	}

	public void setRi(Register ri) {
		this.ri = ri;
	}

	public Register getRe() {
		return re;
	}

	public void setRe(Register re) {
		this.re = re;
	}

	public Register getRo() {
		return ro;
	}

	public void setRo(Register ro) {
		this.ro = ro;
	}

	public Register getMop() {
		return mop;
	}

	public void setMop(Register mop) {
		this.mop = mop;
	}

	public Memory getMemory() {
		return memory;
	}

	public void setMemory(Memory memory) {
		this.memory = memory;
	}

	public Boolean isRunning() {
		return running;
	}

	public void setRunning(Boolean running) {
		this.running = running;
	}
}
