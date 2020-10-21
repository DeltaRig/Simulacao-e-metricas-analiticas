import java.util.ArrayList;

public class QueueStructure {
	
	/* Static variable representing a destination from a queue to the outside of the system.
	 * See destinos ArrayList below.*/
	public static final QueueStructure EXIT = new QueueStructure("EXIT", -1,-1,-1,-1,-1,-1,null); 
	
	
	public String id;
    public int servers;
    public int capacity;
    
    //Time intervals for events
    public double arrivalMin;
    public double arrivalMax;
    public double serviceMin;
    public double serviceMax;
    
    // A queue may have one or more destinos for departure
    public ArrayList<QueueStructure> destinos;
    // Each destination has it's corresponding routing probability
    public ArrayList<Double> probabilidadeDestino;
    
    //------------------------------------------------------------
    // Simulation state variables
    //------------------------------------------------------------
    /* Because the capacity of the queue can be infinite, the list containing
     * the time spent on each state cannot be defined before runtime. It might grow
     * with each client arrival. Therefore I've decided to keep currentQueueSize private
     * and provide methods to modify it.*/
    private int currentQueueSize;
    // List that keeps track of the amount of time the queue spent in each given state:
    public ArrayList<Double> temposDeEstado;
    public int clientsLost;
    
	public QueueStructure(String id, int servers, int capacity, double arrivalMin, double arrivalMax, double serviceMin,
			double serviceMax, ArrayList<QueueStructure> destinos) {
		this.id = id;
		this.servers = servers;
		this.capacity = capacity;
		this.arrivalMin = arrivalMin;
		this.arrivalMax = arrivalMax;
		this.serviceMin = serviceMin;
		this.serviceMax = serviceMax;
		if(destinos==null) {
			this.destinos = new ArrayList<>();
		} else {
			this.destinos = destinos;
		}
		this.probabilidadeDestino = new ArrayList<>();
		resetSimulationVariables();
	}
	
	public boolean isFull() {
		return currentQueueSize>=capacity;
	}
	
	public boolean canServeOnArrival() {
		return currentQueueSize <= servers;
	}
	
	public boolean canServeOnDeparture() {
		return currentQueueSize >= servers;
	}
	
	/* If client can enter the queue: increments currentQueueSize, adds another state to stateTime if necessary
	 * and returns true.
	 * Otherwise: increments clientsLost and returns false. */
	public void addClient() {
		currentQueueSize++;
		/* If this is the first time the queue has entered this state, add the
		 * state to the list of state times. */
		if(temposDeEstado.size()<currentQueueSize+1) {
			temposDeEstado.add(0.0);
		}
	}
	
	public void removeClient() {
		currentQueueSize--;
	}
	
	public void updateQueueTimes(double variacaoTempo) {
		temposDeEstado.set(currentQueueSize, temposDeEstado.get(currentQueueSize) + variacaoTempo);
	}
	
	public void resetSimulationVariables() {
		currentQueueSize = 0;
		temposDeEstado = new ArrayList<>();
		temposDeEstado.add(0.0); //Add time for the beginning state 0.
		clientsLost = 0;
	}
	
	public String toString() {
		return String.format(
				"Servers:%d\nCapacity:%d\nArrivals:%.1f to %.1f\nService:%.1f to %.1f\nTransfers clients to another queue:%b\n",
				servers, capacity, arrivalMin, arrivalMax, serviceMin, serviceMax, (destinos!=null));
	}
	
	
}