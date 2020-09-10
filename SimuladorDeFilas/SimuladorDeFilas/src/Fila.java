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
	private double chegadaMin;
	private double chegadaMax;
	private double atendiMin;
	private double atendiMax;
	private int estadoAtual; //quantidade de pessoas na fila

	public Fila(int servidores, int capacidade, double chegadaMin, double chegadaMax, double atendiMin,	double atendiMax) {
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

	public double getChegadaMin() {
		return chegadaMin;
	}

	public void setChegadaMin(double chegadaMin) {
		this.chegadaMin = chegadaMin;
	}

	public double getChegadaMax() {
		return chegadaMax;
	}

	public void setChegadaMax(double chegadaMax) {
		this.chegadaMax = chegadaMax;
	}

	public double getAtendiMin() {
		return atendiMin;
	}

	public void setAtendiMin(double atendiMin) {
		this.atendiMin = atendiMin;
	}

	public double getAtendiMax() {
		return atendiMax;
	}

	public void setAtendiMax(double atendiMax) {
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
