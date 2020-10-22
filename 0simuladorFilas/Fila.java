import java.util.ArrayList;

public class Fila {
	
	/* Static variable representing a destination from a queue to the outside of the system.
	 * See destinos ArrayList below.*/
	public static final Fila FIM = new Fila("FIM", -1,-1,-1,-1,-1,-1,null); 
	
	
	public String id;
    public int servidores;
    public int capacidade;
    
    //Time intervals for events
    public double minChegada;
    public double maxChegada;
    public double minServico;
    public double maxServico;
    
    // A queue may have one or more destinos for departure
    public ArrayList<Fila> destinos;
    // Each destination has it's corresponding routing probability
    public ArrayList<Double> probabilidadeDestino;
    
    //------------------------------------------------------------
    // Simulation state variables
    //------------------------------------------------------------
    /* Because the capacidade of the queue can be infinite, the list containing
     * the time spent on each state cannot be defined before runtime. It might grow
     * with each client arrival. Therefore I've decided to keep estadoAtualDaFila private
     * and provide methods to modify it.*/
    private int estadoAtualDaFila;
    // List that keeps track of the amount of time the queue spent in each given state:
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
	
	public boolean canServeOnArrival() {
		return estadoAtualDaFila <= servidores;
	}
	
	public boolean canServeOnDeparture() {
		return estadoAtualDaFila >= servidores;
	}
	
	/* If client can enter the queue: increments estadoAtualDaFila, adds another state to stateTime if necessary
	 * and returns true.
	 * Otherwise: increments perda and returns false. */
	public void addClient() {
		estadoAtualDaFila++;
		/* If this is the first time the queue has entered this state, add the
		 * state to the list of state times. */
		if(temposDeEstado.size()<estadoAtualDaFila+1) {
			temposDeEstado.add(0.0);
		}
	}
	
	public void removeClient() {
		estadoAtualDaFila--;
	}
	
	public void atualizaTempoFila(double variacaoTempo) {
		temposDeEstado.set(estadoAtualDaFila, temposDeEstado.get(estadoAtualDaFila) + variacaoTempo);
	}
	
	public void reiniciaVariaveisDaSimulador() {
		estadoAtualDaFila = 0;
		temposDeEstado = new ArrayList<>();
		temposDeEstado.add(0.0); //Add time for the beginning state 0.
		perda = 0;
	}
	
	@Override
	public String toString() {
		return String.format(
				"Serviço:%d \nCapacidade:%d \nChegada:%.1f a %.1f \nServiço:%.1f a %.1f \nPassagem de clientes:%b\n",
				servidores, capacidade, minChegada, maxChegada, minServico, maxServico, (destinos != null));
	}
	
	
}