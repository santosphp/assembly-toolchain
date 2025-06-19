package classes;

public class Registrador {
	private int valor;
	private int tamanho;
	private String identificador;
	
	public int getValor() {
		return valor;
	}
	public void setValor(int valor) {
		this.valor = valor;
	}
	public int getTamanho() {
		return tamanho;
	}
	public void setTamanho(int tamanho) {
		this.tamanho = tamanho;
	}
	public String getIdentificador() {
		return identificador;
	}
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}
	
	public Registrador(int valor, int tamanho, String identificador) {
		super();
		this.valor = valor;
		this.tamanho = tamanho;
		this.identificador = identificador;
	}
	public Registrador() {
		super();
	}
	
	public int carregarValor(int valor) {
		// implementar
		return 0;
	}
	
	public int ler() {
		// implementar
		return 0;
	}
	
	
}
