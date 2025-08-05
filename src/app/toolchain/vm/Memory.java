package app.toolchain.vm;

import java.util.List;
import java.util.ArrayList;

public class Memory {
	
	private int size;
	private List<Integer> memoryCells = new ArrayList<>();
	private int stackBaseAddress;
	private int stackMaxSize;
	private Register sp;
	
	public Memory(List<Integer>data){
		this.size = 1024;
		
		// SP starts with zero to indicate it points to the base of the stack, aka memoryCells[2]
		this.sp = new Register(0, 16, "SP");
		this.stackBaseAddress = 2;
		this.stackMaxSize = 4;
		
		// Initializes R0 with stackMaxSize and R1 with 1, simply for debug purposes
		memoryCells.add(stackMaxSize);
		memoryCells.add(1);
		
		// Fills the stack with zeros, not necessary, but helps with debug
		while(memoryCells.size() < stackBaseAddress + stackMaxSize) {
			memoryCells.add(0);
		}
		
		// Loads the program after the stack area
		memoryCells.addAll(data);
		
		// Fills the rest of the memory with zeros
		while(memoryCells.size() < size) {
			memoryCells.add(0);
		}
	}
	
	public int read(int address) {
		if(address < 0 || address >= size) {
			throw new IndexOutOfBoundsException("Endereço inválido");
		}
		return memoryCells.get(address);
	}
	
	public void write(int address, int value) {
		if(address == stackBaseAddress) {
			System.out.println("Não pode sobrescrever o endereco base da pilha!");
			return ; 
		}
		if(address < 0 || address >= size) {
			throw new IndexOutOfBoundsException("Endereço inválido");
		}
		memoryCells.set(address, value);
	}
	
	public void push(int value) {
		int spValue = sp.read();
		if(checkOverflow()) {
			System.out.println("Overflow! Desviando para endereço 0.");
			return ;
		}
		sp.loadValue(spValue + 1);
		memoryCells.set(stackBaseAddress + sp.read(), value);
	}
	
	public int pop(){
		int spValue = sp.read();
		if (spValue == 0) {
			System.out.println("Stack Underflow! Pilha vazia.");
			return -1;
		}
		int value = memoryCells.get(spValue + stackBaseAddress);
		sp.loadValue(spValue - 1);
		return value;
	}
	
	public boolean checkOverflow() {
		return sp.read() >= stackMaxSize;
	}
	
	public void dumpPilha() {
	    int topo = sp.read();
	    if (topo == 0) {
	        System.out.println("Pilha está vazia.");
	        return;
	    }
	    
	    System.out.print("Pilha (do fundo ao topo): ");
	    for (int i = 1; i <= topo; i++) {
	        System.out.print(memoryCells.get(stackBaseAddress + i) + " ");
	    }
	    System.out.println();
	}
	
	// This list will be useful for the GUI
	public List<Integer> getStackContents() {
	    List<Integer> stack = new ArrayList<>();
	    int topAddress = sp.read() + stackBaseAddress;
	    for (int i = topAddress; i >= stackBaseAddress; --i) {
	        stack.add(memoryCells.get(i));
	    }
	    return stack;
	}

	public Register getSp() {
		return sp;
	}
	
	
	public int getSize() {
		return size;
	}
	
	public int getBase() { 
		return stackBaseAddress; }
}