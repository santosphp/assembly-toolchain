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
	private Register r0;
	private Register r1;
	private Register mop;
	private Memory memory;
	
	// Methods
	public Boolean executeInstruction() {
		Instruction currentInst = new Instruction();
		currentInst.setOpcode(memory.read(pc.read()));
		pc.loadValues(pc.read() + 1);
		
		currentInst.setAddrMode(decodeAdressingMode(currentInst.getOpcode()));
		ri.loadValues(currentInst.getOpcode() & 0x1F);

		// 0 imediato
		// 1 direto
		// 2 indireto 1°
		// 3 indireto 2º
		// 4 indireto ambos
		
		switch(ri.read()) {
		case 0x02: // ADD
			// Program op1
			currentInst.setOperand(1, memory.read(pc.read()));
			pc.loadValues(pc.read() + 1);

			// op1 direto
			if(currentInst.getAddrMode() == 1) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}

			// op1 indireto
			if(currentInst.getAddrMode() == 2) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}
			
			acc.loadValues(acc.read() + currentInst.getOperand(1));
			break;
			
		case 0x00: // BR
			// Program op1
			currentInst.setOperand(1, memory.read(pc.read()));
			pc.loadValues(pc.read() + 1);
			
			// op1 direto
			currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));

			// op1 indireto
			if(currentInst.getAddrMode() == 2) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}

			pc.loadValues(currentInst.getOperand(1));
			break;
			
		case 0x05: // BRNEG
			// Program op1
			currentInst.setOperand(1, memory.read(pc.read()));
			pc.loadValues(pc.read() + 1);

			// op1 direto
			currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));

			// op1 indireto
			if(currentInst.getAddrMode() == 2) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}
			
			if(acc.read() < 0) {
				pc.loadValues(currentInst.getOperand(1));
			}
			
			break;
			
		case 0x01: //BRPOS
			// Program op1
			currentInst.setOperand(1, memory.read(pc.read()));
			pc.loadValues(pc.read() + 1);

			// op1 direto
			currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));

			// op1 indireto
			if(currentInst.getAddrMode() == 2) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}
			
			if(acc.read() > 0) {
				pc.loadValues(currentInst.getOperand(1));
			}
			
			break;
		
		case 0x04: //BRZERO
			// Program op1
			currentInst.setOperand(1, memory.read(pc.read()));
			pc.loadValues(pc.read() + 1);

			// op1 direto
			currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));

			// op1 indireto
			if(currentInst.getAddrMode() == 2) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}
			
			if(acc.read() == 0) {
				pc.loadValues(currentInst.getOperand(1));
			}
			
			break;
			
		case 0x15: //CALL
			// Program op1
			currentInst.setOperand(1, memory.read(pc.read()));
			pc.loadValues(pc.read() + 1);

			// op1 direto
			currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));

			// op1 indireto
			if(currentInst.getAddrMode() == 2) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}
			
			// Testar pilha...
			memory.write(sp.read(), pc.read());
			sp.loadValues(sp.read() + 1);
			
			pc.loadValues(currentInst.getOperand(1));
			
			break;
			
		case 0x13: //COPY
			// Program op1
			currentInst.setOperand(1, memory.read(pc.read()));
			pc.loadValues(pc.read() + 1);
			// Program op2
			currentInst.setOperand(2, memory.read(pc.read()));
			pc.loadValues(pc.read() + 1);

			// op1 direto
			currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));

			// op1 indireto
			if(currentInst.getAddrMode() == 2) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}
			
			// op2 direto
			if(currentInst.getAddrMode() == 1) {
				currentInst.setOperand(2, memory.read(currentInst.getOperand(2)));
			}

			// op2 indireto
			if(currentInst.getAddrMode() == 3) {
				currentInst.setOperand(2, memory.read(currentInst.getOperand(2)));
				currentInst.setOperand(2, memory.read(currentInst.getOperand(2)));
			}
			
			memory.write(currentInst.getOperand(1), currentInst.getOperand(2));
			
			break;
			
		}
		
		// ...
		
		return true;
	}
	
	public void setMop(Integer mop) {
		this.mop.loadValues(mop);
	}
	
	public Map<String, Integer> getRegistersState() {
		Map<String, Integer> registersState = new HashMap<>();
		registersState.put(this.pc.getIdentifier(), this.pc.read());
		registersState.put(this.sp.getIdentifier(), this.sp.read());
		registersState.put(this.acc.getIdentifier(), this.acc.read());
		registersState.put(this.ri.getIdentifier(), this.ri.read());
		registersState.put(this.re.getIdentifier(), this.re.read());
		registersState.put(this.r0.getIdentifier(), this.r0.read());
		registersState.put(this.r1.getIdentifier(), this.r1.read());
		registersState.put(this.mop.getIdentifier(), this.mop.read());
		return registersState;
	}
	
	public Integer decodeAdressingMode(Integer opcode) {
		// Implementation
		// 0 imediato
		// 1 direto
		// 2 indireto 1°
		// 3 indireto 2º
		// 4 indireto ambos
		int addrMode = 0;
		
		if(((opcode & 0x40) == 1) && ((opcode & 0x20) == 1)) {
			addrMode = 4;
		}
		else if((opcode & 0x40) == 1) {
			addrMode = 3;
		}
		else if((opcode & 0x20) == 1) {
			addrMode = 2;
		}
		else if((opcode & 0x80) == 1) {
			addrMode = 0;
		}
		else {
			addrMode = 1;
		}

		return addrMode;
	}
	
	// Constructor
	public Cpu(int mopValue, List<Integer> programData) {
		super();
		this.pc = new Register(64, 16, "PC");
		this.sp = new Register(2, 16, "SP");
		this.acc = new Register(0, 16, "ACC");
		this.ri = new Register(0, 16, "RI");
		this.re = new Register(0, 16, "RE");  // Setado na leitura do arquivo...
		this.r0 = new Register(0, 16, "R0");
		this.r1 = new Register(0, 16, "R1");
		this.mop = new Register(mopValue, 16, "MOP");
		this.memory = new Memory(16, 2048, programData);
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

	public Register getR0() {
		return r0;
	}

	public void setR0(Register r0) {
		this.r0 = r0;
	}

	public Register getR1() {
		return r1;
	}

	public void setR1(Register r1) {
		this.r1 = r1;
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
}
