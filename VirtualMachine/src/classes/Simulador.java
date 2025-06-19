package classes;

import java.util.ArrayList;

import classes.Cpu;
import classes.Instrucao;

public class Simulador {
	private Cpu cpu;
	private ArrayList<Instrucao> arquivoCodigo;
	public Cpu getCpu() {
		return cpu;
	}
	public void setCpu(Cpu cpu) {
		this.cpu = cpu;
	}
	public ArrayList<Instrucao> getArquivoCodigo() {
		return arquivoCodigo;
	}
	public void setArquivoCodigo(ArrayList<Instrucao> arquivoCodigo) {
		this.arquivoCodigo = arquivoCodigo;
	}
	public Simulador(Cpu cpu, ArrayList<Instrucao> arquivoCodigo) {
		super();
		this.cpu = cpu;
		this.arquivoCodigo = arquivoCodigo;
	}
	public Simulador() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public void carregaArquivo() {
		//implementar
	}
	
	public void iniciaSimulacao() {
		//implementar
	}
}
