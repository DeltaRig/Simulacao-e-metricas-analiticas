/**
 * Desenvolvedoras: Daniela Rigoli e Franciele Constante
 * Turma: 128
 * Professor: Afonso Sales
*/

import java.util.ArrayList;

class Resultados {
    
	String[] idFila;
	String[] descricao;
	double tempoTotalSimulacao;

    ArrayList<ArrayList<Double>> temposDeEstado;
    
    double[] perda;
                       
    public Resultados(String[] idFila, double t, ArrayList<ArrayList<Double>> st, double[] cl) {
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
        	result.append(String.format("Perda: %.2f\n\n\n", perda[i]));
        }
        
        result.append(String.format("Tempo total de simulação: %.2f", tempoTotalSimulacao));
        return result.toString();
    }
    
    public void somaSimulacao(Resultados r) {
    	tempoTotalSimulacao += r.tempoTotalSimulacao;
    	
    	//Faz a soma de clientes perdidos para cada fila simulada e o tempo gasto nas simulações
    	for(int i = 0; i < temposDeEstado.size(); i++) {
    		perda[i] += r.perda[i];
    		
    		ArrayList<Double> temposFila1 = temposDeEstado.get(i);
    		ArrayList<Double> temposFila2 = r.temposDeEstado.get(i);
    		comparadorArrays(temposFila1, temposFila2);
    		for(int j = 0; j<temposFila1.size(); j++) {
    			temposFila1.set(j, temposFila1.get(j) + temposFila2.get(j));
    		}
    	}
    }
	
	//Faz a divisão dos campos por n
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
	
	//Deixa as matrizes com tamanhos iguais, preenchendo com zeros a menor.
    private void comparadorArrays(ArrayList<Double> arr, ArrayList<Double> outroArr) {
    	while(arr.size()<outroArr.size())
			arr.add(0.0);
    	while(outroArr.size()<arr.size())
			outroArr.add(0.0);
    	
    }
}





