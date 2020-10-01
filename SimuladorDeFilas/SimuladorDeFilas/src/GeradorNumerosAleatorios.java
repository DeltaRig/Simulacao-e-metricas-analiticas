/**
 * Nomes: Daniela Rigoli e Franciele Constante
 * Simulação e Métricas Analíticas - 128
 * Afonso Sales
 * Versão 10/09/2020
 */
import java.util.Random;

public class GeradorNumerosAleatorios {
	private double[] alets = {1, 2, 3, 4, 5};
	private int count;
	private int turno;

	private int a;
	private int c;
	private int m;
	private long x;


	//implementar para o usuário poder informar valores aleatórios para semente
	public GeradorNumerosAleatorios() {
		a = 4903917;
        c = 41;
		m = Integer.MAX_VALUE;	
		turno = 0;
		x = (long) alets[turno];
		count = 0;

	}

	public int getAletsLength(){
		return alets.length;
	}

	
	public int getTurno(){
		return turno;
	}

	public void contaTurno(){
		turno++;
		x = (long) alets[turno];
	}

	//para usar sementes
	public double recebeAletEntre() {
		double low = 0;
		double high = 1;
		double result = alets[count] * (high - low) + low;
		count++;
		return result;
		
	}
	// para usar aleatorios
	public double recebeAletEntre(double low, double high) {
		x = Long.remainderUnsigned(a * x + c, m);

		double res = x / m;
		return res;		
	}


}

