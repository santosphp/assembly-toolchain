package app.toolchain.vm;

import java.util.List;
import java.util.ArrayList;

public class Memory {
	
	private int size;
	private final int word;
	private List<Integer> datas = new ArrayList<>();
	private int base;
	private int limit;
	private Register sp;
	
	public Memory(List<Integer>data){
		this.size = 1024;
		this.word = 16;
		this.base = 2;
		this.limit = 64;
		this.sp = new Register(2, 16, "SP");
		datas.add(limit);
		datas.add(0);
		
		while(datas.size() < limit) {
			datas.add(0);
		}
		//this.datas = data;
		for(int i=0; i<data.size(); i++) {
			datas.add(data.get(i));
		}
		
		while(datas.size() < size) {
			datas.add(0);
		}
		/*
		datas.set(base, limit);
		*/
	}
	
	public int read(int address) {
		if(address < 0 || address >= size) {
			throw new IndexOutOfBoundsException("Endereço inválido");
		}
		return datas.get(address);
	}
	
	public void write(int address, int value) {
		if(address == base) {
			System.out.println("Não pode sobrescrever o endereco base da pilha!");
			return ; 
		}
		if(address < 0 || address >= size) {
			throw new IndexOutOfBoundsException("Endereço inválido");
		}
		datas.set(address, value);
	}
	
	public void push(int value) {
		int spValue = sp.read();
		if(checkOverflow()) {
			System.out.println("Overflow! Desviando para endereço 0.");
			return ;
		}
		sp.loadValue(spValue + 1);
		datas.set(base + sp.read(), value);
	}
	
	public int pop(){
		int spValue = sp.read();
		if (spValue == 0) {
			System.out.println("Stack Underflow! Pilha vazia.");
			return -1;
		}
		int value = datas.get(spValue + base);
		sp.loadValue(spValue - 1);
		return value;
	}
	
	public boolean checkOverflow() {
		return sp.read() >= limit;
	}
	
	public void dumpPilha() {
	    int topo = sp.read();
	    if (topo == 0) {
	        System.out.println("Pilha está vazia.");
	        return;
	    }
	    
	    System.out.print("Pilha (do fundo ao topo): ");
	    for (int i = 1; i <= topo; i++) {
	        System.out.print(datas.get(base + i) + " ");
	    }
	    System.out.println();
	}

	public Register getSp() {
		return sp;
	}
	
	public int getSize() {
		return size;
	}
}