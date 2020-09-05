/**
 * Nomes: Daniela Rigoli e Franciele Constante
 * Simulação e Métricas Analíticas - 128
 * Afonso Sales
 * Versão 10/09/2020
 */

public class Evento {
	private int tipo; // armazana se a operação será de saída ou chegada | chegada=1  saida=0
	private double tempo;
	
	public Evento(int tipo, double tempo) {
		super();
		this.tipo = tipo;
		this.tempo = tempo;
	}
	public int getTipo() {
		return tipo;
	}
	public double getTempo() {
		return tempo;
	}

}
