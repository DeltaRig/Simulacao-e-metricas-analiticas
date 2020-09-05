/**
 * Nomes: Daniela Rigoli e Franciele Constante
 * Simulação e Métricas Analíticas - 128
 * Afonso Sales
 * Versão 10/09/2020
 */
import java.util.Random;

public class GeradorNumerosAleatorios {
	private double seed;

	//implementar para o usuário poder informar valores aleatórios para semente
	public GeradorNumerosAleatorios() {
		Random pseudo = new Random();
		seed = pseudo.nextDouble();
	}

	public double recebeAletEntre(double low, double high) {
		double result = pseudoAleatorio(seed) * (high - low) + low;
		return result;
	}

	public static double pseudoAleatorio(double random) {
        final double euler = Math.E;

        random += Math.pow(random, euler) + Math.pow(2, Math.PI) + 0.31;
        random = random * 10;
        random = random - Math.floor(random);
        return random;   
    }	
}
