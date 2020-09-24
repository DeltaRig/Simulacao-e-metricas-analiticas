import java.text.DecimalFormat;
import java.util.ArrayList;

public class Resultados {
    private int perdaM; // usado para contar as perdas
	private double tempoSimulM; // tempo total da simulação
    private double percentM, estadoTempM; 

    private ArrayList<Integer> perdas;
    private ArrayList<Double> tempoSimul;
    private ArrayList<Double> percent;
    private ArrayList<Double> estadoTemp;
    
    public Resultados() {
        perdaM = 0;
        tempoSimulM = 0;
        percentM = 0;
        estadoTempM = 0;

        perdas = new ArrayList<Integer>();
        tempoSimul = new ArrayList<Double>();
        percent = new ArrayList<Double>();
        estadoTemp = new ArrayList<Double>();
    }

    public void addResults(int pd, double t, double pt, double e){
        perdas.add(pd);
        tempoSimul.add(t);
        percent.add(pt);
        estadoTemp.add(e);
    }

    public void calculaMedias(){
        for(int i = 0; i < perdas.size(); i++){ //considera que todos tem o mesmo tamanho
            perdaM += perdas.get(i);
            tempoSimulM += tempoSimul.get(i);
            percentM += percent.get(i);
            estadoTempM += estadoTemp.get(i);
        }
        perdaM = perdaM / perdas.size();
        tempoSimulM = tempoSimulM / tempoSimul.size();
        percentM = percentM / percent.size();
        estadoTempM = estadoTempM / estadoTemp.size();
    }

    public String resultadoFinal(int servidores, int capacidade, Fila fila, ArrayList<Double> estadoFila){
        DecimalFormat df = new DecimalFormat("###.###");
        
        String aux = "G/G/" + servidores + "/" + capacidade + "\n";
		aux += "Estado\t\tTotal\t\tPorcent\n";
		for (int i = 0; i <= fila.getCapacidade(); i++) {
			estadoTempM = estadoFila.get(i);
			percentM = estadoTempM * 100 / tempoSimulM;
			aux += i+ "\t\t" + estadoTempM + "\t\t" +  percentM + "\n";
		}
		aux += "\nPerdas: " + perdaM;
        aux += "\nTempo Total: " + tempoSimulM ;
        return aux;
    }

    
}
