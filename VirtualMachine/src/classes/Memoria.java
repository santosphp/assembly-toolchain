package classes;

import java.util.ArrayList;

public class Memoria {
	private int tamanho;
	private int palavra;
	private ArrayList<Integer> dados;
	public int getTamanho() {
		return tamanho;
	}
	public void setTamanho(int tamanho) {
		this.tamanho = tamanho;
	}
	public int getPalavra() {
		return palavra;
	}
	public void setPalavra(int palavra) {
		this.palavra = palavra;
	}
	public ArrayList<Integer> getDados() {
		return dados;
	}
	public void setDados(ArrayList<Integer> dados) {
		this.dados = dados;
	}
	public Memoria(int tamanho, int palavra, ArrayList<Integer> dados) {
		super();
		this.tamanho = tamanho;
		this.palavra = palavra;
		this.dados = dados;
	}
	public Memoria(int tamanho, int palavra) {
		super();
		this.tamanho = tamanho;
		this.palavra = palavra;
		this.dados = new ArrayList<Integer>();
	}
	
	public Memoria() {
		super();
	}
	
	public int ler(int endereco, int valor) {
		// implementar
		return 0;
	}
	
	public int escrever(int endereco, int valor) {
		// implementar
		return 0;
	}
}
