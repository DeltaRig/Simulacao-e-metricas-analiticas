/**
 * Nomes: Daniela Rigoli e Franciele Constante
 * Simulação e Métricas Analíticas - 128
 * Afonso Sales
 * Versão 10/09/2020
 */
import java.util.Random;

public class GeradorNumerosAleatorios {
	private double seed;
	private double[] alets = { 0.2176, 0.0103, 0.1109, 0.3456, 0.9910, 0.2323, 0.9211, 0.0322, 0.1211, 0.5131};
	private int count;

	//implementar para o usuário poder informar valores aleatórios para semente
	public GeradorNumerosAleatorios() {
		//Random pseudo = new Random();
		//seed = pseudo.nextDouble();
		count = 0;

	}

	public int getOperacao(){
		return alets.length;
	}

	public double recebeAletEntre(double low, double high) {
		double result = alets[count] * (high - low) + low;
		count++;
		return result;
		
	}

	/** 
	public static double pseudoAleatorio(double random) {
        final double euler = Math.E;

        random += Math.pow(random, euler) + Math.pow(2, Math.PI) + 0.31;
        random = random * 10;
        random = random - Math.floor(random);
		return random;   
		
    }	*/
}

