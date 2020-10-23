import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Comparator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Simulador {
	
    private ArrayList<Fila> estruturaFila;
    private ArrayList<Long> sementes;
    private int alets;
    private ArrayList<Escalonador> primChegada;
	
    public Simulador(ArrayList<Fila> estruturaFila, ArrayList<Long> sementes, int alets, ArrayList<Escalonador> primChegada) {
    	this.estruturaFila = estruturaFila;
    	this.sementes = sementes;
    	this.alets = alets;
    	this.primChegada = primChegada;
    }
    
    //Gera o objeto a partir do arquivo
    public Simulador(String nomeArquivo) throws Exception {
    	recebeArquivo(nomeArquivo);
    }
    
	//Cria o relatório de resultados acumulando os resultados de cada simulação.
	public void iniciaSimulacao() {
		String[] idsFila = new String[estruturaFila.size()];
		ArrayList<ArrayList<Double>> temposDeEstado = new ArrayList<>();
		for(int i = 0; i < estruturaFila.size(); i++) {
			temposDeEstado.add(new ArrayList<Double>());
			idsFila[i] = estruturaFila.get(i).id;
		}
		Resultados res = new Resultados(idsFila, 0.0, temposDeEstado, new double[estruturaFila.size()]);
		
		
		for(long s : sementes) {
			Resultados tempo = iniciaSimulacao(s, alets);
			res.somaSimulacao(tempo);
		}

		//Obtém a média dos resultados através da divisão dos resultados acumulados pelo nº
		//de simulações executadas.
		res.reiniciaVariaveisDaSimulador(sementes.size());
		System.out.printf("Resultados de %d simulações:\n", sementes.size());
		System.out.println(res.toString());
	}
	
	private Resultados iniciaSimulacao(long sementeAleatoria, int totalalets) {
		
		//Agenda os eventos e fornece as primeiras chegadas
		Comparator<Escalonador> comparadorAgendamento = new Escalonador();
		PriorityQueue<Escalonador> agendamento = new PriorityQueue<>(comparadorAgendamento);
		
        for(Escalonador x : primChegada) {
        	agendamento.offer(x);
        }
               
        GeradorNumerosAleatorios num = new GeradorNumerosAleatorios(sementeAleatoria);
        double tempo = 0;
        
        while(totalalets > 0) {
            
            //Executa o próximo agendado
            Escalonador esc = agendamento.poll();
            double variacaoTempo = esc.tempo - tempo;
            for(Fila f : estruturaFila) {
            	f.atualizaTempoFila(variacaoTempo);
            }
            tempo += variacaoTempo; //Atualiza o tempo de simulação
            
            
            if(esc.evento == TipoEvento.CHEGADA) {
                Fila dest = esc.destino;
            	if(!dest.cheio()) { //Fila apta para receber o cliente
                    dest.addCliente();
                    if(dest.agendaServicoNaChegada()) { //Fila apta para atender o cliente
                        totalalets -= agendaSaida(agendamento, dest, tempo, num);
                    }
                } else { //Fila cheia
                    dest.perda++;
                }
                agendaChegada(agendamento, dest, tempo, num);
                totalalets--;
            
            
            
            } else if(esc.evento == TipoEvento.PASSAGEM) {
            	Fila origem = esc.origem;
            	Fila dest = esc.destino;
            	origem.removeCliente();
                if(origem.agendaServicoNaSaida()) { //Pode atender outro cliente
                	totalalets -= agendaSaida(agendamento, origem, tempo, num);
                }
                if(!dest.cheio()) {
                	dest.addCliente();
                	if(dest.agendaServicoNaChegada()) {
                		totalalets -= agendaSaida(agendamento, dest, tempo, num);
                	}
            	} else { //Cliente perdido, destino está cheio
            		dest.perda++;
            	}
            
            
                
            } else {
            	Fila ori = esc.origem;
            	ori.removeCliente();
            	if(ori.agendaServicoNaSaida()) { //Pode atender mais um cliente
            		totalalets -= agendaSaida(agendamento, ori, tempo, num);
            	}
            }
        }
        
        //Fim da simulação, gera o relatório.
        ArrayList<ArrayList<Double>> tempoFilas = new ArrayList<>();
        double[] perda = new double[estruturaFila.size()];
        String[] idsFila = new String[estruturaFila.size()];
        for(int i=0; i<estruturaFila.size(); i++) {
        	idsFila[i] = estruturaFila.get(i).id;
        	tempoFilas.add(estruturaFila.get(i).temposDeEstado);
        	perda[i] = estruturaFila.get(i).perda;
        }
        
        Resultados sr = new Resultados(idsFila, tempo, tempoFilas, perda);
		
		//Zera o estado de todas as filas
        for(Fila f : estruturaFila) {
        	f.reiniciaVariaveisDaSimulador();
        }
        
        return sr;
	}
	
	private void agendaChegada(PriorityQueue<Escalonador> escalonador, Fila destino, double time, GeradorNumerosAleatorios alet) {
		double numAlet = alet.next();
		double eventoTime = time + (destino.maxChegada - destino.minChegada) * numAlet + destino.minChegada;
		escalonador.offer(Escalonador.chegada(eventoTime, destino));
	}
	
	private int agendaSaida(PriorityQueue<Escalonador> escalonador, Fila filaDeOrigem, double tempo, GeradorNumerosAleatorios alet) {
		//Define evento time
		double numAlet = alet.next();
		int aletsUsado = 1;
		double eventoTime = tempo + (filaDeOrigem.maxServico-filaDeOrigem.minServico) * numAlet + filaDeOrigem.minServico;
		
		Fila dest = null;
	    //Caso encontre mais de um caminho possível, lista as probabilidades e consome um aleatório
		if(filaDeOrigem.destinos.size()>1) {
			double randomProb = alet.next();
			aletsUsado++;
			double probabilidade = 0;
			//Adiciona p1 na lista, vê se aleatório é menor. Se não, adiciona p2 e verifica de novo.
			//Segue verificando e adicionando até encontrar um aleatório menor.
			//Quando encontrar o aleatório menor é definido o destino.
			for(int i = 0; i<filaDeOrigem.probabilidadeDestino.size(); i++) {
				probabilidade += filaDeOrigem.probabilidadeDestino.get(i);
				if(randomProb < probabilidade) {
					dest = filaDeOrigem.destinos.get(i);
					break;
				}
			}
		
		} else {
			dest = filaDeOrigem.destinos.get(0);
		}
		
		//Agenda os eventos
		if(dest == Fila.FIM) {
			escalonador.offer(Escalonador.saida(eventoTime, filaDeOrigem));
		} else { //Mudança de fila
			escalonador.offer(Escalonador.passagem(eventoTime, filaDeOrigem, dest));
		}
		
		return aletsUsado;
	}
	
	
	
	
	
	
	//Receber e identificar dados do arquivo
	private void recebeArquivo(String nomeArquivo) throws Exception {
		try {
			estruturaFila = new ArrayList<>();
			sementes = new ArrayList<>();
			primChegada = new ArrayList<>();
			String data = new String(Files.readAllBytes(Paths.get(nomeArquivo)));
			String[] lines = data.split("(\r\n)|\n"); 
			for(int i=0; i<lines.length; i++) {
				try {
					String s = lines[i];
					if(s.length()==0 || s.charAt(0)=='#') { //linha de comentario então não precisa fazer nenhuma outra verificação
						continue;
						
					} else if(s.charAt(0)=='q') { //Nessa linha aparece uma fila
						estruturaFila.add(criaFila(s));
						
					} else if(s.charAt(0)=='d'){ //Nessa linha aparece destino
						int chegada = s.indexOf(':');
						defineDestino(estruturaFila, s.substring(chegada+1));
						
					} else if(s.charAt(0)=='s') { //Nessa linha aparecem as sementes
						int chegada = s.indexOf(':');
						defineSementes(s.substring(chegada+1));
					
					} else if(s.charAt(0)=='a') { //Quantidade de aleatorios usados
						int chegada = s.indexOf(':');
						alets = Integer.parseInt(s.substring(chegada+1).trim());
						
					} else if(s.charAt(0)=='p') { //Nessa linha é definido a primeira chegada
						int chegada = s.indexOf(':');
						defineprimChegada(s.substring(chegada+1));
						
					} else {
						throw new Exception(
								"Erro de sintaxe inválida.");
					}
				} catch(Exception e) {
					throw new Exception(String.format(
							"Erro encontrado durante a leitura do arquivo (file line: %d): %s", i+1, e.toString()));
				}
			}
			
			if(primChegada.isEmpty()) {
				throw new Exception("Não foi definido a primeira chegada");
			}
		} catch(IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
	
	private Fila criaFila(String s) throws Exception {
		
			String[] splitOnColon = s.replaceAll("\\s", "").split(":");
			String[] parametros = splitOnColon[1].split("/");
			int capacidade;
			if(parametros[1].equals("inf")) { //referece a infinito
				capacidade = Integer.MAX_VALUE;
			} else {
				capacidade = Integer.parseInt(parametros[1]);
			}
			return new Fila(
					splitOnColon[0], //id
					Integer.parseInt(parametros[0]), //servers
					capacidade,
					Double.parseDouble(parametros[2]), //minChegada
					Double.parseDouble(parametros[3]), //maxChegada
					Double.parseDouble(parametros[4]), //minServico					
					Double.parseDouble(parametros[5]), //maxServico
					null); //destinos
	}
	
	private void defineDestino(ArrayList<Fila> estruturaFila, String s) throws Exception {
		String[] estruturaFilaConect = s.split("(->)");
		
		//Define a fila de origem analisando os destinos
		String nomeOrigem = estruturaFilaConect[0].trim();
		Fila origem = buscaFila(estruturaFila, nomeOrigem);
		
		String[] destinos = estruturaFilaConect[1].split(",");

		for(String d : destinos) {
			Fila dest = null;
			String[] destAndProb = d.split("/");
			String destName = destAndProb[0].trim();
			if(destName.equals("S") || destName.equals("s")) { //Destino é a saída
				dest = Fila.FIM;
			} else { //Destino é uma das filas
				dest = buscaFila(estruturaFila, destName);
			}
			origem.destinos.add(dest);
			origem.probabilidadeDestino.add(Double.parseDouble(destAndProb[1]));
		}
		double soma = 0.0;
		for(double p : origem.probabilidadeDestino) {
			soma += p;
		}
		if(soma != 1.0) {
			throw new Exception("A soma das probabilidades de roteamento de ser igual a 1.");
		}
	}
	
	private Fila buscaFila(ArrayList<Fila> estruturaFila, String nome) throws Exception{
		for(Fila f : estruturaFila) {
			if(f.id.equals(nome)) return f;
		}
		throw new Exception(String.format(
				"A fila \"%s\" não foi definida no arquivo.", nome));
		
	}
	
	private void defineSementes(String str) {
		String[] longsString = str.replaceAll("\\s", "").split(",");
		for(String s : longsString) {
			sementes.add(Long.parseLong(s));
		}
	}
	
	private void defineprimChegada(String str) throws Exception{
		for(String s : str.replaceAll("\\s", "").split(",")) {
			String[] filaArr = s.split("/");
			double tempo = Double.parseDouble(filaArr[1]);
			Fila f = buscaFila(estruturaFila, filaArr[0]);
			primChegada.add(Escalonador.chegada(tempo, f));
		}
	}
}


















