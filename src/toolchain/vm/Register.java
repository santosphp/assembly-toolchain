package toolchain.vm;

public class Register {
	// Attributes
	private int value;
	private int size;
	private String id;
	
	// Constructor
	public Register(int value, int size, String id) {
		super();
		this.value = value;
		this.size = size;
		this.id = id;
	}

	// Getters and Setters
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
