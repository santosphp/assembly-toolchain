package app.toolchain.vm;

public class Register {

	private int value;
	private int size;
	private String identifier;

	public Register(int value, int size, String identifier) {
		this.identifier = identifier;
		this.size = size;
		this.value = value;
	}

	public void loadValue(int value) {
		int mask = (1 << size) - 1;
		this.value = value & mask;
	}

	public int read() {
		return value;
	}

	public String getIdentifier() {
		return identifier;
	}

}
