package classes;

import java.util.ArrayList;
import classes.Cpu;

public class Instrucao {
	private int opcode;
	private ArrayList<Integer> operandos;
	private int modeEndereçamento;
	private int tamanho;
	
	public int getOpcode() {
		return opcode;
	}
	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}
	public ArrayList<Integer> getOperandos() {
		return operandos;
	}
	public void setOperandos(ArrayList<Integer> operandos) {
		this.operandos = operandos;
	}
	public int getModeEndereçamento() {
		return modeEndereçamento;
	}
	public void setModeEndereçamento(int modeEndereçamento) {
		this.modeEndereçamento = modeEndereçamento;
	}
	public int getTamanho() {
		return tamanho;
	}
	public void setTamanho(int tamanho) {
		this.tamanho = tamanho;
	}
	public Instrucao(int opcode, ArrayList<Integer> operandos, int modeEndereçamento, int tamanho) {
		super();
		this.opcode = opcode;
		this.operandos = operandos;
		this.modeEndereçamento = modeEndereçamento;
		this.tamanho = tamanho;
	}
	public Instrucao() {
		super();
	}
	public Instrucao(int opcode, int modeEndereçamento, int tamanho) {
		super();
		this.operandos = new ArrayList<Integer>();
		this.opcode = opcode;
		this.modeEndereçamento = modeEndereçamento;
		this.tamanho = tamanho;
	}
	
	public void executar(Cpu cpu) {
		// implementar
	}
	
	
}
