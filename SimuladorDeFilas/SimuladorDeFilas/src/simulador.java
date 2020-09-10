/**
 * Nomes: Daniela Rigoli e Franciele Constante
 * Simulação e Métricas Analíticas - 128
 * Afonso Sales
 * Versão 10/09/2020
 */

import java.util.ArrayList;
import java.util.Scanner;

public class simulador {
	static Scanner entrada = new Scanner(System.in);

	static GeradorNumerosAleatorios geradorNums = new GeradorNumerosAleatorios();

	static Fila fila;

	static ArrayList<Double> estadoFila = new ArrayList<>(); 
	static ArrayList<Evento> listaEvento = new ArrayList<>();
	static ArrayList<Double> numerosAleatorios = new ArrayList<>();
	
	static final int OPERACAO = 100000;
	static int perda = 0; // usado para contar as perdas
	static int atendidos = 0;
	static double tempoSimul = 0; // tempo total da simulação
	static double intervTempo = 0; // intervalo entre os tempos
	static double percent, estadoTemp; 


	public static void main(String[] args) {
		// Dados necessários | arrumar para puxar de um arquivo depois
		System.out.println("Digite a quantidade de servidores da fila: ");
		int servidores = entrada.nextInt();
		System.out.println("Digite a capacidade maxima da fila: ");
		int capacidade = entrada.nextInt();
		System.out.println("Digite o tempo de chegada min: ");
		double chegaMin = entrada.nextInt();
		System.out.println("Digite o tempo de chegada max: ");
		double chegaMax = entrada.nextInt();
		System.out.println("Digite o tempo de atendimento min: ");
		double atendiMin = entrada.nextInt();
		System.out.println("Digite o tempo de atendimento max: ");
		double atendiMax = entrada.nextInt();

		fila = new Fila(servidores, capacidade, chegaMin, chegaMax, atendiMin, atendiMax);

		System.out.println(fila.toString());

		System.out.println("Digite o tempo para a primeira chegada: ");
		double primChega = entrada.nextDouble();

		// gera os números aleatórios necessários para a quant de operações
		for (int i = 0; i < geradorNums.getOperacao(); i++) {
			numerosAleatorios.add(geradorNums.recebeAletEntre(0, 1));
		}

		// define tamanho da fila
		for (int i = 0; i < capacidade + 1; i++) {
			estadoFila.add(0.0);
		}

		filaSimples(fila, primChega);
		System.out.println("G/G/" + servidores + "/" + capacidade + "\ngit");
		System.out.println("Estado\t\tTotal\t\tPorcent");
		for (int i = 0; i <= fila.getCapacidade(); i++) {
			estadoTemp = estadoFila.get(i);
			percent = estadoTemp * 100 / tempoSimul;
			System.out.printf("%d\t\t%.2f\t\t%.2f\n", i, estadoTemp, percent);
		}
		System.out.println("Perdas: " + perda);
		System.out.printf("Tempo Total: %.2f", tempoSimul);

	}


	private static void filaSimples(Fila fi, double inicio) {
		tempoSimul = 0;
		chegada(inicio);
		double menorTempo = 0;
		int posicaoMenor = 0;

		
		while (!numerosAleatorios.isEmpty()) {
			// verifica o primeiro evento, verifica se fila esta cheia e se o proximo evento não é de chegada
			// remove o evento e agendaa próxima chegada
			if (fila.getEstadoAtual() == fila.getCapacidade()) {

				menorTempo = listaEvento.get(0).getTempo();
				posicaoMenor = 0;
				for (int i = 0; i < listaEvento.size(); i++) {

					if (listaEvento.get(posicaoMenor).getTempo() > listaEvento.get(i).getTempo()) {
						menorTempo = listaEvento.get(i).getTempo();
						posicaoMenor = i;
					}
				}
				// se evento não for de saída
				if (listaEvento.get(posicaoMenor).getTipo() == 1) {
					perda++;

					listaEvento.remove(posicaoMenor);
					chegada(menorTempo);
				} else {
					saida(menorTempo);
					listaEvento.remove(posicaoMenor);
				}

			} else { // fila não estando cheia
				menorTempo = listaEvento.get(0).getTempo();
				posicaoMenor = 0;
				for (int i = 0; i < listaEvento.size(); i++) {
					if (listaEvento.get(i).getTempo() < listaEvento.get(posicaoMenor).getTempo()) {
						menorTempo = listaEvento.get(i).getTempo();
						posicaoMenor = i;
					}

				}

				if (listaEvento.get(posicaoMenor).getTipo() == 1) {
					chegada(menorTempo);
					listaEvento.remove(posicaoMenor);
				}
				else if (listaEvento.get(posicaoMenor).getTipo() == 0) {
					saida(menorTempo);
					listaEvento.remove(posicaoMenor);
				}
				menorTempo = 0;
				posicaoMenor = 0;
			}
		}

	}

	private static void chegada(double tempo) {
		int pessoasNaFila = fila.getEstadoAtual();

		contabilizaTempo(tempo);
		if (fila.getEstadoAtual() < fila.getCapacidade()) {
			fila.setEstadoAtual(pessoasNaFila + 1);
			if (fila.getEstadoAtual() <= fila.getServidores()) {
				agendaSaida();
			}
		}
		agendaChegada();
	}

	private static void saida(double tempo) {

		contabilizaTempo(tempo);
		int posFila = fila.getEstadoAtual();
		fila.setEstadoAtual(posFila - 1);
		atendidos++;
		if (fila.getServidores() <= fila.getEstadoAtual()) {
			agendaSaida();
		}
	}

	private static void agendaChegada() {
		double aux = numerosAleatorios.remove(0);  //utiliza na operação
		double resultado = tempoSimul + (((fila.getChegadaMax() - fila.getChegadaMin()) * aux) + fila.getChegadaMin());
		Evento evento = new Evento(1, resultado);
		listaEvento.add(evento);
	}

	private static void agendaSaida() {
		double aux = numerosAleatorios.remove(0);
		double resultado = tempoSimul + (((fila.getAtendiMax() - fila.getAtendiMin()) * aux) + fila.getAtendiMin());

		Evento evento = new Evento(0, resultado);
		listaEvento.add(evento);

	}

	//método usado para guardar os tempos com determinada quantida de pessoas
	private static void contabilizaTempo(double tempo) {
		int pessoasNaFila = fila.getEstadoAtual();

		double tempoAnterior = tempoSimul;
		tempoSimul = tempo; //atualizo o tempo da simulação
		double difTempo = tempoSimul - tempoAnterior; //armazena diferença
		double tempoAux = difTempo + estadoFila.get(pessoasNaFila) ;
		estadoFila.set(pessoasNaFila, tempoAux);
	}

}
