package app.toolchain.vm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CPU {
	private VirtualMachine vm;
	private Register pc;
	private Register sp;
	private Register acc;
	private Register ri;
	private Register re;
	private Register r0;
	private Register r1;
	private Memory memory;

	// Constructor
	public CPU(VirtualMachine vm) {
		super();
		this.vm = vm;
		this.memory = new Memory(new ArrayList<>());
		this.pc = new Register(64, "PC");
		this.sp = new Register(0, "SP");
		this.acc = new Register(0, "ACC");
		this.ri = new Register(0, "RI");
		this.re = new Register(0, "RE");
		this.r0 = new Register(0, "R0");
		this.r1 = new Register(0, "R1");
	}
	
	// Methods
	public Boolean executeInstruction() {
	    syncMemoryToRegisters();  // Atualiza r0/r1 com o valor da memória
		
		Instruction currentInst = new Instruction();

		if(pc.read() > memory.getSize()) {
			System.out.println("PC acessou um endereço fora da memória!!!");
			return false;
		}
		currentInst.setOpcode(memory.read(pc.read()));
		pc.loadValue(pc.read() + 1);
		
		// Atualiza o sp local com o da memória
		sp.loadValue(memory.getSp().read());

		currentInst.setAddrMode(decodeAdressingMode(currentInst.getOpcode()));
		ri.loadValue(currentInst.getOpcode() & 0x1F);

		// 0 imediato
		// 1 direto
		// 2 indireto 1°
		// 3 indireto 2º
		// 4 indireto ambos

		switch (ri.read()) {
		case 2: // ADD
			// Program op1
			currentInst.setOperand(1, memory.read(pc.read()));
			pc.loadValue(pc.read() + 1);

			// op1 direto
			if (currentInst.getAddrMode() == 1) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}

			// op1 indireto
			if (currentInst.getAddrMode() == 2) {
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
			if (currentInst.getAddrMode() == 2) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}

			pc.loadValue(currentInst.getOperand(1));
			break;

		case 5: // BRNEG
			// op1 direto
			currentInst.setOperand(1, memory.read(pc.read()));
			pc.loadValue(pc.read() + 1);

			// op1 indireto
			if (currentInst.getAddrMode() == 2) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}

			if (acc.read() < 0) {
				pc.loadValue(currentInst.getOperand(1));
			}

			break;

		case 1: // BRPOS
			// op1 direto
			currentInst.setOperand(1, memory.read(pc.read()));
			pc.loadValue(pc.read() + 1);

			// op1 indireto
			if (currentInst.getAddrMode() == 2) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}

			if (acc.read() > 0) {
				pc.loadValue(currentInst.getOperand(1));
			}

			break;

		case 4: // BRZERO
			// op1 direto
			currentInst.setOperand(1, memory.read(pc.read()));
			pc.loadValue(pc.read() + 1);

			// op1 indireto
			if (currentInst.getAddrMode() == 2) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}

			if (acc.read() == 0) {
				pc.loadValue(currentInst.getOperand(1));
			}

			break;

		case 15: // CALL
			// op1 direto
			currentInst.setOperand(1, memory.read(pc.read()));
			pc.loadValue(pc.read() + 1);

			// op1 indireto
			if (currentInst.getAddrMode() == 2) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}

			memory.push(pc.read());

			pc.loadValue(currentInst.getOperand(1));

			break;

		case 13: // COPY
			// op1 direto
			currentInst.setOperand(1, memory.read(pc.read()));
			pc.loadValue(pc.read() + 1);

			// op1 indireto
			if (currentInst.getAddrMode() == 2) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}

			// Program op2
			currentInst.setOperand(2, memory.read(pc.read()));
			pc.loadValue(pc.read() + 1);

			// op2 direto
			if (currentInst.getAddrMode() == 1) {
				currentInst.setOperand(2, memory.read(currentInst.getOperand(2)));
			}

			// op2 indireto
			if (currentInst.getAddrMode() == 3) {
				currentInst.setOperand(2, memory.read(currentInst.getOperand(2)));
				currentInst.setOperand(2, memory.read(currentInst.getOperand(2)));
			}

			memory.write(currentInst.getOperand(1), currentInst.getOperand(2));

			break;

		case 10: // DIVIDE
			// Program op1
			currentInst.setOperand(1, memory.read(pc.read()));
			pc.loadValue(pc.read() + 1);

			// op1 direto
			if (currentInst.getAddrMode() == 1) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}

			// op1 indireto
			if (currentInst.getAddrMode() == 2) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}

			acc.loadValue(acc.read() / currentInst.getOperand(1));

			break;

		case 3: // LOAD
			// Program op1
			currentInst.setOperand(1, memory.read(pc.read()));
			pc.loadValue(pc.read() + 1);

			// op1 direto
			if (currentInst.getAddrMode() == 1) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}

			// op1 indireto
			if (currentInst.getAddrMode() == 2) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}

			acc.loadValue(currentInst.getOperand(1));

			break;

		case 14: // MULT
			// Program op1
			currentInst.setOperand(1, memory.read(pc.read()));
			pc.loadValue(pc.read() + 1);

			// op1 direto
			if (currentInst.getAddrMode() == 1) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}

			// op1 indireto
			if (currentInst.getAddrMode() == 2) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}

			acc.loadValue(acc.read() * currentInst.getOperand(1));

			break;

		case 17: // PUSH

			memory.push(acc.read());

			break;

		case 18: // POP

			acc.loadValue(memory.pop());

			break;

		case 12: // READ
			// op1 direto
			currentInst.setOperand(1, memory.read(pc.read()));
			pc.loadValue(pc.read() + 1);

			// op1 indireto
			if (currentInst.getAddrMode() == 2) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}

			// Input exception...
			// Hold execution until inputBuffer contains something
			while(vm.getInputBuffer().isEmpty()) {}
			int input = vm.readInput();
			memory.write(currentInst.getOperand(1), (short) input);

			break;

		case 16: // RET
			
			pc.loadValue(memory.pop());
			break;

		case 11: // STOP
			vm.notifyProgramFinished();
			return false;

		case 7: // STORE
			// op1 direto
			currentInst.setOperand(1, memory.read(pc.read()));
			pc.loadValue(pc.read() + 1);

			// op1 indireto
			if (currentInst.getAddrMode() == 2) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}

			memory.write(currentInst.getOperand(1), acc.read());

			break;

		case 6: // SUB
			// Program op1
			currentInst.setOperand(1, memory.read(pc.read()));
			pc.loadValue(pc.read() + 1);

			// op1 direto
			if (currentInst.getAddrMode() == 1) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}

			// op1 indireto
			if (currentInst.getAddrMode() == 2) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}

			acc.loadValue(acc.read() - currentInst.getOperand(1));

			break;

		case 8: // WRITE
			// Program op1
			currentInst.setOperand(1, memory.read(pc.read()));
			pc.loadValue(pc.read() + 1);

			// op1 direto
			if (currentInst.getAddrMode() == 1) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}

			// op1 indireto
			if (currentInst.getAddrMode() == 2) {
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
				currentInst.setOperand(1, memory.read(currentInst.getOperand(1)));
			}

			
			vm.printOutput(Integer.toString(currentInst.getOperand(1)));
			// CALL Output exception...
			System.out.println("Output: " + currentInst.getOperand(1));

			break;

		default:

			System.out.println("Opcode indefinido: " + ri.read());

			return false;
		}

	    syncMemoryToRegisters();  // Atualiza mudanças em R0 e R1 para a CPU
		return true;
	}

	public int peekNextOpcode() {
		int previousOpCode = memory.read(pc.read() - 1) & 0x1F;
		int opCode = memory.read(pc.read()) & 0x1F;
		
		if (previousOpCode == 11 && vm.isHalted())
			return -1;
		else
			return opCode;
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
	
	public void clearRegisters() {
		this.pc.loadValue(0);
		this.sp.loadValue(0);
		this.acc.loadValue(0);
		this.ri.loadValue(0);
		this.re.loadValue(0);
		this.r0.loadValue(0);
		this.r1.loadValue(0);
	}
	
	public void syncRegistersToMemory() {
	    memory.write(0, r0.read());
	    memory.write(1, r1.read());
	}

	public void syncMemoryToRegisters() {
	    r0.loadValue(memory.read(0));
	    r1.loadValue(memory.read(1));
	}
	
	// Getters
	public Register getPc() {
		return pc;
	}

	public Register getSp() {
		return sp;
	}

	public Register getAcc() {
		return acc;
	}

	public Register getRi() {
		return ri;
	}

	public Register getRe() {
		return re;
	}

	public Register getR0() {
	    r0.loadValue(memory.read(0));
	    return r0;
	}
	public Register getR1() {
	    r1.loadValue(memory.read(1));
	    return r1;
	}
	
	public Memory getMemory() {
		return memory;
	}
	
	// Setters
	public void setPc(Register pc) {
		this.pc = pc;
	}
	
	public void setSp(Register sp) {
		this.sp = sp;
	}
	
	public void setAcc(Register acc) {
		this.acc = acc;
	}

	public void setRi(Register ri) {
		this.ri = ri;
	}
	
	public void setRe(Register re) {
		this.re = re;
	}
	
	public void setR0(Register r0) {
	    this.r0 = r0;
	    memory.write(0, r0.read());
	}
	public void setR1(Register r1) {
	    this.r1 = r1;
	    memory.write(1, r1.read());
	}

	public void setMemory(Memory memory) {
		this.memory = memory;
		setPc(new Register(memory.getCodeSegmentBaseAddress(), "PC"));
		// Mantém o endereço de acesso à memória de dados (registrador interno)
		setRe(new Register(memory.getCodeSegmentBaseAddress(), "RE"));
	}
}
