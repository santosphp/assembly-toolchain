package toolchain.vm;

import java.util.List;

public class Memory {
	// Attributes
	private int wordSize;
	private int memorySize;
	private List<Integer> data;
	
	// Constructor
	public Memory(int wordSize, int memorySize, List<Integer> data) {
		super();
		this.wordSize = wordSize;
		this.memorySize = memorySize;
		this.data = data;
	}

	// Getters and Setters
	public int getWordSize() {
		return wordSize;
	}

	public void setWordSize(int wordSize) {
		this.wordSize = wordSize;
	}

	public int getMemorySize() {
		return memorySize;
	}

	public void setMemorySize(int memorySize) {
		this.memorySize = memorySize;
	}

	public List<Integer> getData() {
		return data;
	}

	public void setData(List<Integer> data) {
		this.data = data;
	}
}
