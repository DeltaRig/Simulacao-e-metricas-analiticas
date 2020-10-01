/**
 * Nomes: Daniela Rigoli e Franciele Constante
 * Simulação e Métricas Analíticas - 128
 * Afonso Sales
 * Versão 10/09/2020
 */

import java.security.cert.TrustAnchor;
import java.util.ArrayList;
import java.util.Scanner;

public class simulador {
	static Scanner entrada = new Scanner(System.in);

	static GeradorNumerosAleatorios geradorNums = new GeradorNumerosAleatorios();

	static Fila fila;
	//criar array

	static Resultados result = new Resultados();

	static ArrayList<Double> estadoFila = new ArrayList<>(); 
	static ArrayList<Evento> listaEvento = new ArrayList<>();
	static ArrayList<Double> numerosAleatorios = new ArrayList<>();
	
	static int turnos = geradorNums.getAletsLength();
	static final int OPERACAO = 100000;
	static int perda = 0; // usado para contar as perdas
	static double tempoSimul = 0; // tempo total da simulação
	static double intervTempo = 0; // intervalo entre os tempos
	static double percent, estadoTemp;

	static final boolean usaSeeds = false;


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

		// define tamanho da fila
		for (int i = 0; i < capacidade + 1; i++) {
			estadoFila.add(0.0);
		}

		//turnos
		for(int i = 0; i < turnos; i++){
			// gera os números aleatórios necessários para a quant de operações
			if(usaSeeds){
				for (int j = 0; j < geradorNums.getAletsLength(); j++) {
					numerosAleatorios.add(geradorNums.recebeAletEntre());
				}
				turnos = 1;
			} else {
				for (int j = 0; j < OPERACAO; j++) {
					numerosAleatorios.add(geradorNums.recebeAletEntre(0, 1));
				}
				System.out.println("\nTurno " + geradorNums.getTurno());
				if(geradorNums.getTurno() < turnos -1){
					geradorNums.contaTurno();
				}
				
			}
			

			//ocorre os eventos
			filaSimples(fila, primChega);
			
			result.addResults(perda, tempoSimul, percent, estadoTemp);

			
		}
		result.calculaMedias();
		System.out.println("\n"+result.resultadoFinal(servidores, capacidade, fila, estadoFila));

		

	}


	private static void filaSimples(Fila fi, double inicio) {
		tempoSimul = 0;
		chegada(inicio);
		double menorTempo = 0;
		int posicaoMenor = 0;
		//getEvento do escalonador - para saber se é chegada ou saida
		//estado < capacidade
		
		while (!numerosAleatorios.isEmpty()) {
			// verifica o primeiro evento, verifica se fila esta cheia e se o proximo evento não é de chegada
			// remove o evento e agendaa próxima chegada
			
			if (fila.getEstadoAtual() < fila.getCapacidade()) { // fila não estando cheia
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
				
			} else {  //resulta em perda
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
