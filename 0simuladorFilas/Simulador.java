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
    
    /* Constructs object from file. */
    public Simulador(String nomeArquivo) throws Exception {
    	recebeArquivo(nomeArquivo);
    }
    
	public void runSimulation() {
		/* Creates a new SimulationReport With all fields zeroed
		 * This report will accumulate the results of every simulation.
		 * Begin by creating the list containing, for each queue in the simulation,
		 * the list of time spent on each state of said queue. Since a queue can have
		 * infinite capacidade, this list is created containing empty lists
		 * (lists with no times for any state). */
		String[] idsFila = new String[estruturaFila.size()];
		ArrayList<ArrayList<Double>> temposDeEstado = new ArrayList<>();
		for(int i=0; i<estruturaFila.size(); i++) {
			temposDeEstado.add(new ArrayList<Double>());
			idsFila[i] = estruturaFila.get(i).id;
		}
		SimulationReport res = new SimulationReport(idsFila, 0.0, temposDeEstado, new double[estruturaFila.size()]);
		
		
		for(long r : sementes) {
			SimulationReport tempo = runSimulation(r, alets);
			res.sumSimulation(tempo);
		}
		/* Divide the accumulated results by the number of simulations run to obtain
		 * the average of the results. */
		res.averageResults(sementes.size());
		System.out.printf("Printing average results of %d simulations:\n", sementes.size());
		System.out.println(res.toString());
	}
	
	private SimulationReport runSimulation(long aletseed, int totalalets) {
		
		//evento schedule
		Comparator<Escalonador> schComparator = new Escalonador();
		PriorityQueue<Escalonador> schedule = new PriorityQueue<>(schComparator);
		//First arrivals are offered to the schedule
        for(Escalonador se : primChegada) {
        	schedule.offer(se);
        }
               
        GeradorNumerosAleatorios rng = new GeradorNumerosAleatorios(aletseed);
        double tempo = 0;
        
        while(totalalets > 0) {
            
            //Process next scheduled evento
            Escalonador se = schedule.poll();
            double variacaoTempo = se.tempo - tempo;
            for(Fila q : estruturaFila) {
            	q.updateQueueTimes(variacaoTempo);
            }
            tempo += variacaoTempo; //update simulation clock
            
            
            if(se.evento == TipoEvento.CHEGADA) {
                Fila dest = se.destino;
            	if(!dest.isFull()) { //Queue can receive the client
                    dest.addClient();
                    if(dest.canServeOnArrival()) { //Queue can serve the client
                        totalalets -= scheduleDeparture(schedule, dest, tempo, rng);
                    }
                } else { //Queue full
                    dest.perda++;
                }
                scheduleArrival(schedule, dest, tempo, rng);
                totalalets--;
            
            
            
            } else if(se.evento == TipoEvento.PASSAGEM) {
            	Fila ori = se.origem;
            	Fila dest = se.destino;
            	ori.removeClient();
                if(ori.canServeOnDeparture()) { //Origin can serve another client.
                	totalalets -= scheduleDeparture(schedule, ori, tempo, rng);
                }
                if(!dest.isFull()) { //destino can take another client.
                	dest.addClient();
                	if(dest.canServeOnArrival()) { //destino can serve another client.
                		totalalets -= scheduleDeparture(schedule, dest, tempo, rng);
                	}
            	} else { //destino full. Client lost.
            		dest.perda++;
            	}
            
            
                
            } else { //It's a departure
            	Fila ori = se.origem;
            	ori.removeClient();
            	if(ori.canServeOnDeparture()) { //Can serve one more client
            		totalalets -= scheduleDeparture(schedule, ori, tempo, rng);
            	}
            }
        }
        
        //Simulation finished. Make report.
        ArrayList<ArrayList<Double>> tempoFilas = new ArrayList<>();
        double[] perda = new double[estruturaFila.size()];
        String[] idsFila = new String[estruturaFila.size()];
        for(int i=0; i<estruturaFila.size(); i++) {
        	idsFila[i] = estruturaFila.get(i).id;
        	tempoFilas.add(estruturaFila.get(i).temposDeEstado);
        	perda[i] = estruturaFila.get(i).perda;
        }
        
        SimulationReport sr = new SimulationReport(idsFila, tempo, tempoFilas, perda);
        
        //Reset the state of all queues.
        for(Fila q : estruturaFila) {
        	q.resetSimulationVariables();
        }
        
        return sr;
	}
	
	private void scheduleArrival(PriorityQueue<Escalonador> escalonador, Fila destino, double time, GeradorNumerosAleatorios rng) {
		double randomNumber = rng.next();
		double eventoTime = time + (destino.maxChegada - destino.minChegada) * randomNumber + destino.minChegada;
		escalonador.offer(Escalonador.newArrival(eventoTime, destino));
	}
	
	private int scheduleDeparture(PriorityQueue<Escalonador> escalonador, Fila origin, double time, GeradorNumerosAleatorios rng) {
		//Define evento time
		double randomNumber = rng.next();
		int aletsUsado = 1;
		double eventoTime = time + (origin.maxServico-origin.minServico) * randomNumber + origin.minServico;
		
		Fila dest = null;
		/* If more than one possible destino, roll the probabilities.
		 * This consumes an extra random number. */
		if(origin.destinos.size()>1) {
			double randomProb = rng.next();
			aletsUsado++;
			double prob = 0;
			/* Probability check works like this:
			 * Add p1 to prob. Check if random is lower. If not, add p2 to prob and check again.
			 * If not, add p3 and check again and so on. If at any check the random is lower,
			 * the corresponding destino is chosen.*/
			for(int i = 0; i<origin.probabilidadeDestino.size(); i++) {
				prob += origin.probabilidadeDestino.get(i);
				if(randomProb < prob) {
					dest = origin.destinos.get(i);
					break;
				}
			}
		
		// Else there's only one possible destino
		} else {
			dest = origin.destinos.get(0);
		}
		
		//Generate schedule eventos accordingly
		if(dest == Fila.FIM) {//Departure from the system
			escalonador.offer(Escalonador.newDeparture(eventoTime, origin));
		} else { //Passage from one queue to another
			escalonador.offer(Escalonador.newPassage(eventoTime, origin, dest));
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
					if(s.length()==0 || s.charAt(0)=='#') { //Empty or comment line
						continue;
						
					} else if(s.charAt(0)=='q') { //Line defines a queue
						estruturaFila.add(createQueue(s));
						
					} else if(s.charAt(0)=='d'){ //Line defines a destino
						int chegada = s.indexOf(':');
						defineDestino(estruturaFila, s.substring(chegada+1));
						
					} else if(s.charAt(0)=='s') { //Line defines sementes for the rng
						int chegada = s.indexOf(':');
						defineSementes(s.substring(chegada+1));
					
					} else if(s.charAt(0)=='r') { //Line defines the amount of alets to be used
						int chegada = s.indexOf(':');
						alets = Integer.parseInt(s.substring(chegada+1).trim());
						
					} else if(s.charAt(0)=='f') { //Line defines the first arrivals for the queues
						int chegada = s.indexOf(':');
						defineprimChegada(s.substring(chegada+1));
						
					} else { //Sintax error
						throw new Exception(
								"Non empty line contains invalid syntax.");
					}
				} catch(Exception e) {
					throw new Exception(String.format(
							"Exception thrown during file parsing (file line: %d): %s", i+1, e.toString()));
				}
			}
			
			if(primChegada.isEmpty()) {
				throw new Exception("No first arrivals were defined for any queue in the system.");
			}
		} catch(IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
	
	private Fila createQueue(String s) throws Exception {
		
			String[] splitOnColon = s.replaceAll("\\s", "").split(":");
			String[] parametros = splitOnColon[1].split("/");
			int capacidade;
			if(parametros[1].equals("inf")) {
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
		
		//Defines the origin queue whose destinos are being parsed
		String nomeOrigem = estruturaFilaConect[0].trim();
		Fila origem = findQueue(estruturaFila, nomeOrigem);
		
		//Parse destinos
		String[] destinos = estruturaFilaConect[1].split(",");
		//Add each destino with it's corresponding routing probability to the origin object
		for(String d : destinos) {
			Fila dest = null;
			String[] destAndProb = d.split("/");
			String destName = destAndProb[0].trim();
			if(destName.equals("S") || destName.equals("s")) { //destino is the system exit
				dest = Fila.FIM;
			} else { //destino is one of the system's queues
				dest = findQueue(estruturaFila, destName);
			}
			origem.destinos.add(dest);
			origem.probabilidadeDestino.add(Double.parseDouble(destAndProb[1]));
		}
		double soma = 0.0;
		for(double p : origem.probabilidadeDestino) {
			soma += p;
		}
		if(soma != 1.0) {
			throw new Exception("The sum of all routing probabilities in a queue must equal 1.");
		}
	}
	
	private Fila findQueue(ArrayList<Fila> estruturaFila, String nome) throws Exception{
		for(Fila f : estruturaFila) {
			if(f.id.equals(nome)) return f;
		}
		throw new Exception(String.format(
				"Queue \"%s\" does not exist or wasn't previously defined in input file.", nome));
		
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
			Fila f = findQueue(estruturaFila, filaArr[0]);
			primChegada.add(Escalonador.newArrival(tempo, f));
		}
	}
}


















