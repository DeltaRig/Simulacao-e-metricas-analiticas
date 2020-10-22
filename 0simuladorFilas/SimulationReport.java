import java.util.ArrayList;

/* Records results of one simulation. These results can be combined into average results. */
class SimulationReport {
    
	String[] idFila;
	String[] descricao;
	double tempoTotalSimulacao;
    /* This list holds, for each queue in the simulation, a list of
     * the times it spent on each of it's respective states. */
    ArrayList<ArrayList<Double>> temposDeEstado;
    
    //Used a double for cases where the report is an average of different results:
    double[] perda;
                       
    public SimulationReport(String[] idFila, double t, ArrayList<ArrayList<Double>> st, double[] cl) {
        this.idFila = idFila;
    	tempoTotalSimulacao = t;
        temposDeEstado = st;
        perda = cl;
    }
    
    public String toString() {
        StringBuilder result = new StringBuilder(500);
        for(int i = 0; i < temposDeEstado.size(); i++) {
        	ArrayList<Double> temposFila = temposDeEstado.get(i);
            result.append("Fila " + idFila[i]+":\n");
        	for(int j=0; j < temposFila.size(); j++) {
        		result.append(String.format(
                           "Estado: %d  Tempo: %.2f  Probabilidade: %.2f%%\n",
                           j, temposFila.get(j), 100*temposFila.get(j)/tempoTotalSimulacao));
        	}
        	result.append(String.format("Perda: %.2f\n---------------------------\n", perda[i]));
        }
        
        result.append(String.format("Tempo total de simulação: %.2f", tempoTotalSimulacao));
        return result.toString();
    }
    
    /* Sums the results of the second report on the first report. */
    public void sumSimulation(SimulationReport r) {
    	tempoTotalSimulacao += r.tempoTotalSimulacao;
    	
    	// For each queue simulated...
    	for(int i = 0; i < temposDeEstado.size(); i++) {
    		// sum the number of clients lost
    		perda[i] += r.perda[i];
    		
    		/* and for each state of that queue, sum the time spent in both simulations.
    		 * Since queues can have infinite capacity, states may differ.
    		 * This must taken into account: */
    		ArrayList<Double> temposFila1 = temposDeEstado.get(i);
    		ArrayList<Double> temposFila2 = r.temposDeEstado.get(i);
    		comparadorArrays(temposFila1, temposFila2); //Make list sizes equal
    		for(int j = 0; j<temposFila1.size(); j++) {
    			temposFila1.set(j, temposFila1.get(j) + temposFila2.get(j));
    		}
    	}
    }
    
    /* Divides the fields of the report by n. */
    public void reiniciaVariaveisDaSimulador(int n) {
    	tempoTotalSimulacao /= n;
    	for(int i=0; i < temposDeEstado.size(); i++) {
    		perda[i] /= n;
    		ArrayList<Double> tempoDeEstado = temposDeEstado.get(i);
    		for(int j = 0; j < tempoDeEstado.size(); j++) {
    			tempoDeEstado.set(j, tempoDeEstado.get(j) / n);
    		}
    	}
    }
    
    /* Equalizes the size of two arrays by filling the smaller array with zeroes. */
    private void comparadorArrays(ArrayList<Double> arr, ArrayList<Double> outroArr) {
    	while(arr.size()<outroArr.size())
			arr.add(0.0);
    	while(outroArr.size()<arr.size())
			outroArr.add(0.0);
    	
    }
}





