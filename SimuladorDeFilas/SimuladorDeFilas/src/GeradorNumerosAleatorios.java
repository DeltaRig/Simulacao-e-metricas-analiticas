/**
 * Nomes: Daniela Rigoli e Franciele Constante
 * Simulação e Métricas Analíticas - 128
 * Afonso Sales
 * Versão 10/09/2020
 */
import java.util.Random;

public class GeradorNumerosAleatorios {
	private double[] alets = {1, 2, 3, 4, 5};
	private int cont;
	private int turno;
	private final boolean NUM_DEFIDO;
	static int OPERACAO;

	private int a;
	private int c;
	private int m;
	private long x;


	//implementar para o usuário poder informar valores aleatórios para semente
	public GeradorNumerosAleatorios(boolean numDefinido, double[] alets) {
		a = 4903917;
        c = 41;
		m = Integer.MAX_VALUE;	
		turno = 0;
		//System.out.println("Turno" + turno);
		x = (long) alets[turno];
		cont = 0;

		this.alets = alets;

		NUM_DEFIDO = numDefinido;

		if(NUM_DEFIDO){
			OPERACAO = alets.length;
		} else {
			OPERACAO = 100000;
		}

	}

	public int maxTurnos(){
		if(NUM_DEFIDO){
			return 1;
		} else {
			return alets.length;
		}
	}

	
	public int getTurno(){
		return turno;
	}

	public int getCont() {
		return cont;
	}

	public int getOperacoes(){
		return OPERACAO;
	}

	public void contaTurno(){
		turno++;
		x = (long) alets[turno];
		System.out.println("Turno" + turno);
	}

	//para usar sementes
	public double next() {
		double low = 0;
		double high = 1;
		if(NUM_DEFIDO){
			double result = alets[cont] * (high - low) + low;
			cont++;
			return result;
		} else {
			x = Long.remainderUnsigned(a * x + c, m);
			cont++;
			double res = x / m;
			return res;	
		}
		
		
	}

	

	


}

