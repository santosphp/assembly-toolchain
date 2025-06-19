package classes;
import classes.Registrador;

import java.util.Map;

import classes.Memoria;

public class Cpu {
	private Registrador pc;
	private Registrador sp;
	private Registrador acc;
	private Registrador ri;
	private Registrador re;
	private Registrador ro;
	private Registrador mop;
	private Memoria memoria;
	
	
	public Registrador getPc() {
		return pc;
	}
	public void setPc(Registrador pc) {
		this.pc = pc;
	}
	public Registrador getSp() {
		return sp;
	}
	public void setSp(Registrador sp) {
		this.sp = sp;
	}
	public Registrador getAcc() {
		return acc;
	}
	public void setAcc(Registrador acc) {
		this.acc = acc;
	}
	public Registrador getRi() {
		return ri;
	}
	public void setRi(Registrador ri) {
		this.ri = ri;
	}
	public Registrador getRe() {
		return re;
	}
	public void setRe(Registrador re) {
		this.re = re;
	}
	public Registrador getRo() {
		return ro;
	}
	public void setRo(Registrador ro) {
		this.ro = ro;
	}
	public Registrador getMop() {
		return mop;
	}
	public void setMop(Registrador mop) {
		this.mop = mop;
	}
	public Memoria getMemoria() {
		return memoria;
	}
	public void setMemoria(Memoria memoria) {
		this.memoria = memoria;
	}
	public Cpu(Registrador pc, Registrador sp, Registrador acc, Registrador ri, Registrador re, Registrador ro,
			Registrador mop, Memoria memoria) {
		super();
		this.pc = pc;
		this.sp = sp;
		this.acc = acc;
		this.ri = ri;
		this.re = re;
		this.ro = ro;
		this.mop = mop;
		this.memoria = memoria;
	}
	public Cpu() {
		super();
	}
	
	public void executarInstrucao() {
		//implementar
	}
	
	public void setModoOperacao(Registrador modo) {
		//implementar
	}
	
	private Registrador decodificarModoEndereçamento(int opcode) {
		//implementar
		return mop;
	}
	
	//public Map<String> getEstadoRegistradores() {
	//	//implementar
	//}
	
}
