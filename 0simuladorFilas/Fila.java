import java.util.ArrayList;

public class Fila {
	
	public static final Fila FIM = new Fila("FIM", -1,-1,-1,-1,-1,-1,null); 
	
	public String id;
    public int servidores;
    public int capacidade;
    
    public double minChegada;
    public double maxChegada;
    public double minServico;
    public double maxServico;
    
    public ArrayList<Fila> destinos;
    public ArrayList<Double> probabilidadeDestino;
    
	/*estadoAtualDaFila ficará privado devido ao fato da fila poder ser infinita,
	 *não sendo possível definir o tempo gasto antes do tempo de execução.
	 *Será modificada através de métodos conforme a necessidade.*/
    private int estadoAtualDaFila;
    public ArrayList<Double> temposDeEstado;
    public int perda;
    
	public Fila(String id, int servidores, int capacidade, double minChegada, double maxChegada, double minServico,
			double maxServico, ArrayList<Fila> destinos) {
		this.id = id;
		this.servidores = servidores;
		this.capacidade = capacidade;
		this.minChegada = minChegada;
		this.maxChegada = maxChegada;
		this.minServico = minServico;
		this.maxServico = maxServico;

		if(destinos==null) {
			this.destinos = new ArrayList<>();
		} else {
			this.destinos = destinos;
		}

		this.probabilidadeDestino = new ArrayList<>();
		reiniciaVariaveisDaSimulador();
	}
	
	public boolean cheio() {
		return estadoAtualDaFila >= capacidade;
	}
	
	public boolean agendaServicoNaChegada() {
		return estadoAtualDaFila <= servidores;
	}
	
	public boolean agendaServicoNaSaida() {
		return estadoAtualDaFila >= servidores;
	}
	
	/*Verifica se o cliente pode entrar na fila e incrementa estadoAtualDaFila.
	 *Adiciona outro estado de tempo se for necessário.
	 *Caso não possa o cliente é perdido e retorna falso. */
	public void addCliente() {
		estadoAtualDaFila++;
		
		if(temposDeEstado.size()<estadoAtualDaFila+1) {
			temposDeEstado.add(0.0);
		}
	}
	
	public void removeCliente() {
		estadoAtualDaFila--;
	}
	
	public void atualizaTempoFila(double variacaoTempo) {
		temposDeEstado.set(estadoAtualDaFila, temposDeEstado.get(estadoAtualDaFila) + variacaoTempo);
	}
	
	public void reiniciaVariaveisDaSimulador() {
		estadoAtualDaFila = 0;
		temposDeEstado = new ArrayList<>();
		temposDeEstado.add(0.0);
		perda = 0;
	}
	
	@Override
	public String toString() {
		return String.format(
				"Serviço:%d \nCapacidade:%d \nChegada:%.1f a %.1f \nServiço:%.1f a %.1f \nPassagem de clientes:%b\n",
				servidores, capacidade, minChegada, maxChegada, minServico, maxServico, (destinos != null));
	}
	
	
}