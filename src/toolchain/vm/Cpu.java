package toolchain.vm;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

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
		System.out.println(currentInst.getOpcode());
		
		pc.loadValue(pc.read() + 1);
		
		System.out.println(pc.read());
		
		currentInst.setAddrMode(decodeAdressingMode(currentInst.getOpcode()));
		ri.loadValue(currentInst.getOpcode() & 0x1F);

		// 0 imediato
		// 1 direto
		// 2 indireto 1°
		// 3 indireto 2º
		// 4 indireto ambos
		
		switch(ri.read()) {
		case 2: // ADD
			// Program op1
			currentInst.setOperand(1, memory.read(pc.read()));
			pc.loadValue(pc.read() + 1);

			// op1 direto
			if(currentInst.getAddrMode() == 1) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}

			// op1 indireto
			if(currentInst.getAddrMode() == 2) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}
			
			acc.loadValue(acc.read() + currentInst.getOperand(1));
			break;
			
		case 0: // BR
			// op1 direto
			currentInst.setOperand(1, memory.read(pc.read()));
			pc.loadValue(pc.read() + 1);
			
			// op1 indireto
			if(currentInst.getAddrMode() == 2) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}

			pc.loadValue(currentInst.getOperand(1));
			break;
			
		case 5: // BRNEG
			// op1 direto
			currentInst.setOperand(1, memory.read(pc.read()));
			pc.loadValue(pc.read() + 1);

			// op1 indireto
			if(currentInst.getAddrMode() == 2) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}
			
			if(acc.read() < 0) {
				pc.loadValue(currentInst.getOperand(1));
			}
			
			break;
			
		case 1: //BRPOS
			// op1 direto
			currentInst.setOperand(1, memory.read(pc.read()));
			pc.loadValue(pc.read() + 1);

			// op1 indireto
			if(currentInst.getAddrMode() == 2) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}
			
			if(acc.read() > 0) {
				pc.loadValue(currentInst.getOperand(1));
			}
			
			break;
		
		case 4: //BRZERO
			// op1 direto
			currentInst.setOperand(1, memory.read(pc.read()));
			pc.loadValue(pc.read() + 1);
			
			// op1 indireto
			if(currentInst.getAddrMode() == 2) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}
			
			if(acc.read() == 0) {
				pc.loadValue(currentInst.getOperand(1));
			}
			
			break;
			
		case 15: //CALL
			// op1 direto
			currentInst.setOperand(1, memory.read(pc.read()));
			pc.loadValue(pc.read() + 1);

			// op1 indireto
			if(currentInst.getAddrMode() == 2) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}
			
			// Testar pilha...
			memory.write(sp.read(), pc.read());
			sp.loadValue(sp.read() + 1);
			
			pc.loadValue(currentInst.getOperand(1));
			
			break;
			
		case 13: //COPY
			// op1 direto
			currentInst.setOperand(1, memory.read(pc.read()));
			pc.loadValue(pc.read() + 1);

			// op1 indireto
			if(currentInst.getAddrMode() == 2) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}
			
			// Program op2
			currentInst.setOperand(2, memory.read(pc.read()));
			pc.loadValue(pc.read() + 1);
			
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
			
		case 10: //DIVIDE
			// Program op1
			currentInst.setOperand(1, memory.read(pc.read()));
			pc.loadValue(pc.read() + 1);

			// op1 direto
			if(currentInst.getAddrMode() == 1) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}

			// op1 indireto
			if(currentInst.getAddrMode() == 2) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}
			
			acc.loadValue(acc.read() / currentInst.getOperand(1));
			
			break;
			
		case 3: //LOAD
			// Program op1
			currentInst.setOperand(1, memory.read(pc.read()));
			pc.loadValue(pc.read() + 1);
			System.out.println(currentInst.getOperand(1));

			// op1 direto
			if(currentInst.getAddrMode() == 1) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}

			// op1 indireto
			if(currentInst.getAddrMode() == 2) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}
			
			acc.loadValue(currentInst.getOperand(1));
			
			break;
			
		case 14: //MULT
			// Program op1
			currentInst.setOperand(1, memory.read(pc.read()));
			pc.loadValue(pc.read() + 1);

			// op1 direto
			if(currentInst.getAddrMode() == 1) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}

			// op1 indireto
			if(currentInst.getAddrMode() == 2) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}
			
			acc.loadValue(acc.read() * currentInst.getOperand(1));
			
			break;
			
		case 17: //PUSH ???
			
			// Testar pilha...
			memory.write(sp.read(), acc.read());
			sp.loadValue(sp.read() + 1);
			
			break;
			
		case 18: //POP ???
			
			// Testar pilha...
			acc.loadValue(memory.read(sp.read()));
			sp.loadValue(sp.read() - 1);
			
			break;
			
		case 12: //READ
			// op1 direto
			currentInst.setOperand(1, memory.read(pc.read()));
			pc.loadValue(pc.read() + 1);

			// op1 indireto
			if(currentInst.getAddrMode() == 2) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}
			System.out.println(currentInst.getOperand(1));
			
			// CALL Input exception...
			int inputStream = 10 & 0xFF;
			
			memory.write(currentInst.getOperand(1), inputStream);
			
			break;
			
		case 16: //RET

			// Testar pilha...
			acc.loadValue(memory.read(sp.read()));
			sp.loadValue(sp.read() - 1);
			
			break;
			
		case 11: //STOP

			// Encerramento...
			return false;
			
		case 7: //STORE
			// op1 direto
			currentInst.setOperand(1, memory.read(pc.read()));
			pc.loadValue(pc.read() + 1);

			// op1 indireto
			if(currentInst.getAddrMode() == 2) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}
			
			memory.write(currentInst.getOperand(1), acc.read());
			
			break;
			
		case 6: //SUB
			// Program op1
			currentInst.setOperand(1, memory.read(pc.read()));
			pc.loadValue(pc.read() + 1);

			// op1 direto
			if(currentInst.getAddrMode() == 1) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}

			// op1 indireto
			if(currentInst.getAddrMode() == 2) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}
			
			acc.loadValue(acc.read() - currentInst.getOperand(1));
			
			break;
			
		case 8: //WRITE
			// Program op1
			currentInst.setOperand(1, memory.read(pc.read()));
			pc.loadValue(pc.read() + 1);

			// op1 direto
			if(currentInst.getAddrMode() == 1) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}

			// op1 indireto
			if(currentInst.getAddrMode() == 2) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}
			
			// CALL Output exception...
			System.out.println("Output: " + currentInst.getOperand(1));
			
			break;
			
		default:
			
			System.out.println("Opcode indefinido: " + ri.read());
			
			return false;
		}
		
		// ...
		
		return true;
	}
	
	public void setMop(Integer mop) {
		this.mop.loadValue(mop);
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
	    boolean imediato = (opcode & 0x80) != 0;
	    boolean indireto1 = (opcode & 0x20) != 0;
	    boolean indireto2 = (opcode & 0x40) != 0;
		
	    if (indireto1 && indireto2) {
	        return 4; // indireto ambos
	    } else if (indireto2) {
	        return 3; // indireto 2º
	    } else if (indireto1) {
	        return 2; // indireto 1°
	    } else if (imediato) {
	        return 0; // imediato
	    } else {
	        return 1; // direto (trivial)
	    }

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
		this.memory = new Memory(programData);
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
