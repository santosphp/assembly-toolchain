package app.toolchain.vm;

public class Register {

	private short value;
	private String identifier;

	public Register(int value, String identifier) {
		this.identifier = identifier;
		this.value = (short) value;
	}

	public void loadValue(int value) {
		/* Previously we used tried using a mask to limit the ammount of bits
		   but since MOP is only handled internally and using a mask also
		   complicates dealing with signed numbers, we decided to start
		   using short for everything
		   int mask = (1 << size) - 1;
		   this.value = value; & mask;
		*/
		if (identifier.equals("MOP"))
			this.value = (byte) value;
		else
			this.value = (short) value;
	}

	public int read() {
		return value;
	}

	public String getIdentifier() {
		return identifier;
	}

}
