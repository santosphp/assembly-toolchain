package classes;
import classes.Registrador;

public class SistemaPilha {
	private int base;
	private int limite;
	private Registrador sp;
	public int getBase() {
		return base;
	}
	public void setBase(int base) {
		this.base = base;
	}
	public int getLimite() {
		return limite;
	}
	public void setLimite(int limite) {
		this.limite = limite;
	}
	public Registrador getSp() {
		return sp;
	}
	public void setSp(Registrador sp) {
		this.sp = sp;
	}
	public SistemaPilha(int base, int limite, Registrador sp) {
		super();
		this.base = base;
		this.limite = limite;
		this.sp = sp;
	}
	public SistemaPilha() {
		super();
	}

	public void push(int valor) {
		//implentar
	}
	
	public int pop() {
		//implentar
		return 0;
	}
	
	public boolean verificaOverflow() {
		//implentar
		return true;
	}
	
}
