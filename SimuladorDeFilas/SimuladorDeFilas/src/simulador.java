/**
 * Nomes: Daniela Rigoli e Franciele Constante
 * Simulação e Métricas Analíticas - 128
 * Afonso Sales
 * Versão 10/09/2020
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.cert.TrustAnchor;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class simulador {
	static Scanner entrada = new Scanner(System.in);

	static GeradorNumerosAleatorios geradorNums; //true se for usar números definidos
	static boolean numDefinido;
	static double[] listNums;

	static Fila fila;
	//criar array

	static Resultados result = new Resultados();

	static ArrayList<Double> estadoFila = new ArrayList<>(); 
	static ArrayList<Evento> listaEvento = new ArrayList<>();
	static ArrayList<Double> numerosAleatorios = new ArrayList<>();
	
	static int turnos;
	static int perda = 0; // usado para contar as perdas
	static double tempoSimul = 0; // tempo total da simulação
	static double intervTempo = 0; // intervalo entre os tempos
	static double percent, estadoTemp;


	//variaveis do arquivo de entrada:
	static double primChega = 0;
	
	static int servidores;
	static int capacidade;
	static double chegaMin;
	static double chegaMax;
	static double atendiMin;
	static double atendiMax;

	public static void main(String[] args) {

		//chamar método de leitura
		entrada();
		geradorNums = new GeradorNumerosAleatorios(numDefinido, listNums);
		turnos = geradorNums.maxTurnos();
		
		fila = new Fila(servidores, capacidade, chegaMin, chegaMax, atendiMin, atendiMax);

		System.out.println(fila.toString());


		// define tamanho da fila
		for (int i = 0; i < capacidade + 1; i++) {
			estadoFila.add(0.0);
		}

		//turnos
		for(int i = 0; i < turnos; i++){
			

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
		
		while (geradorNums.getCont() < geradorNums.getOperacoes()) {
			// verifica o primeiro evento, verifica se fila esta cheia e se o proximo evento não é de chegada
			// remove o evento e agendaa próxima chegada
			
			if (fila.getEstadoAtual() < fila.getCapacidade()) { // fila não estando cheia
				menorTempo = listaEvento.get(0).getTempo();
				posicaoMenor = 0;
				for (int i = 0; i < listaEvento.size(); i++) { //verifica qual é o próximo evento
					if (listaEvento.get(i).getTempo() < listaEvento.get(posicaoMenor).getTempo()) {
						menorTempo = listaEvento.get(i).getTempo();
						posicaoMenor = i;
					}

				}

				if (listaEvento.get(posicaoMenor).getTipo() == 1) {
					chegada(menorTempo);
					listaEvento.remove(posicaoMenor);
				} else if (listaEvento.get(posicaoMenor).getTipo() == 0) {
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
		double aux = geradorNums.next();  //utiliza na operação
		double resultado = tempoSimul + (((fila.getChegadaMax() - fila.getChegadaMin()) * aux) + fila.getChegadaMin());
		Evento evento = new Evento(1, resultado);
		listaEvento.add(evento);
	}

	private static void agendaSaida() {
		double aux = geradorNums.next();
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


	public static void entrada(){
		Scanner scan = new Scanner(System.in);
        
		boolean verifica = false;
		String momento = "";
        
        
        do{ 
            verifica = false;
            System.out.println("Digite o caminho para o arquivo (com o nome do arquivo)"
                    + "\nEx.: C:/Users/(nome)/Documents/arquivo.txt");
        
            String path = scan.nextLine();
            
            try(BufferedReader reader = new BufferedReader(new FileReader(path));){
                // ler o arquivo e enviar para a tradução
                for (String linha = reader.readLine(); linha != null; linha = reader.readLine()){
                    if(linha.length() > 0){
						if(linha.contains("rndnumbers:")){
							numDefinido = true;

							linha = reader.readLine(); //vai para próxima linha, que deve começar com "-" quando tiver um numero aleatório
							List<Double> list = new ArrayList<>();

							while(linha.contains("-")){
								linha.trim();
								String[] ops = linha.split(" ");
								double rnd = Double.parseDouble(ops[1]);
								list.add(rnd);

								linha = reader.readLine(); 
							}
							listNums = new double[list.size()]; 
							for(int i = 0; i < listNums.length; i++){
								listNums[i] = list.get(i);
							}
						} else if(linha.contains("seeds:")){
							numDefinido = false;

							linha = reader.readLine(); //vai para próxima linha, que deve começar com "-" quando tiver um numero aleatório
							List<Double> list = new ArrayList<>();

							while(linha.contains("-")){
								linha.trim();
								String[] ops = linha.split(" ");
								double rnd = Double.parseDouble(ops[1]);
								list.add(rnd);

								linha = reader.readLine(); 
							}
							listNums = new double[list.size()]; 
							for(int i = 0; i < listNums.length; i++){
								listNums[i] = list.get(i);
							}

						}else if(linha.contains("arrivals:")){
							linha = reader.readLine(); 
							linha.trim(); 
							String[] ops = linha.split(":");
							primChega = Double.parseDouble(ops[1].trim());
						} else if(linha.contains("queues:")){
							linha = reader.readLine(); //F1

							linha = reader.readLine(); //servers
							linha.trim(); 
							String[] ops = linha.split(":");
							servidores = Integer.parseInt(ops[1].trim());

							linha = reader.readLine(); //capacity
							linha.trim();
							ops = linha.split(":");
							capacidade = Integer.parseInt(ops[1].trim());

							linha = reader.readLine(); //minArrival
							linha.trim();
							ops = linha.split(":");
							chegaMin = Double.parseDouble(ops[1].trim());

							linha = reader.readLine(); //maxArrival
							linha.trim();
							ops = linha.split(":");
							chegaMax = Double.parseDouble(ops[1].trim());

							linha = reader.readLine(); //minService
							linha.trim();
							ops = linha.split(":");
							atendiMin = Double.parseDouble(ops[1].trim());

							linha = reader.readLine(); //maxService
							linha.trim();
							ops = linha.split(":");
							atendiMax = Double.parseDouble(ops[1].trim());
						}

                    }
                }
                reader.close();
                verifica = true;
            } catch (IOException e) {
                System.out.println("O arquivo não foi encontrado");
            }
        }while(verifica == false); // Para quando o arquivo for encontrado
        
        
    }
	
}
