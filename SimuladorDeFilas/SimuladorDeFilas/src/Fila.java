/**
 * Nomes: Daniela Rigoli e Franciele Constante
 * Simulação e Métricas Analíticas - 128
 * Afonso Sales
 * Versão 10/09/2020
 */

public class Fila {
// G/G/C/K
	private int servidores; // C
	private int capacidade; // K
	private int chegadaMin;
	private int chegadaMax;
	private int atendiMin;
	private int atendiMax;
	private int estadoAtual; //quantidade de pessoas na fila

	public Fila(int servidores, int capacidade, int chegadaMin, int chegadaMax, int atendiMin,	int atendiMax) {
		this.servidores = servidores;
		this.capacidade = capacidade;
		this.chegadaMin = chegadaMin;
		this.chegadaMax = chegadaMax;
		this.atendiMin = atendiMin;
		this.atendiMax = atendiMax;
		this.estadoAtual = 0;

	}

	public int getServidores() {
		return servidores;
	}

	public void setServidores(int servidores) {
		this.servidores = servidores;
	}

	public int getCapacidade() {
		return capacidade;
	}

	public void setCapacidade(int cap) {
		this.capacidade = cap;
	}

	public int getChegadaMin() {
		return chegadaMin;
	}

	public void setChegadaMin(int chegadaMin) {
		this.chegadaMin = chegadaMin;
	}

	public int getChegadaMax() {
		return chegadaMax;
	}

	public void setChegadaMax(int chegadaMax) {
		this.chegadaMax = chegadaMax;
	}

	public int getAtendiMin() {
		return atendiMin;
	}

	public void setAtendiMin(int atendiMin) {
		this.atendiMin = atendiMin;
	}

	public int getAtendiMax() {
		return atendiMax;
	}

	public void setAtendiMax(int atendiMax) {
		this.atendiMax = atendiMax;
	}

	public int getEstadoAtual() {
		return estadoAtual;
	}

	public void setEstadoAtual(int estadoAtual) {
		this.estadoAtual = estadoAtual;
	}

	@Override
	public String toString() {
		return "Dados da fila:\nServidores = " + servidores + ";\n" +
			"Capacidade = " + capacidade + ";\n" +
			"Tempo mínimo de chegada = " + chegadaMin + ";\n" +
			"Tempo máximo de chegada = " + chegadaMax + ";\n" +
			"Tempo mínimo de atendimento = " + atendiMin + ";\n" +
			"Tempo máximo de atendimento = " + atendiMax + ";\n" +
			"Estado atual da fila = " + estadoAtual + "\n";
	}
	
	

}
