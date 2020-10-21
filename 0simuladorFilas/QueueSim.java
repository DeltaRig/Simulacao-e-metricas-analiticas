import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Comparator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class QueueSim {
	
	
    private ArrayList<QueueStructure> estruturaFila;
    private ArrayList<Long> seeds;
    private int alets;
    private ArrayList<ScheduleEntry> primChegada;
	
    public QueueSim(ArrayList<QueueStructure> estruturaFila, ArrayList<Long> seeds, int alets, ArrayList<ScheduleEntry> primChegada) {
    	this.estruturaFila = estruturaFila;
    	this.seeds = seeds;
    	this.alets = alets;
    	this.primChegada = primChegada;
    }
    
    /* Constructs object from file. */
    public QueueSim(String nomeArquivo) throws Exception {
    	parseFile(nomeArquivo);
    }
    
	public void runSimulation() {
		/* Creates a new SimulationReport With all fields zeroed
		 * This report will accumulate the results of every simulation.
		 * Begin by creating the list containing, for each queue in the simulation,
		 * the list of time spent on each state of said queue. Since a queue can have
		 * infinite capacity, this list is created containing empty lists
		 * (lists with no times for any state). */
		String[] idsFila = new String[estruturaFila.size()];
		ArrayList<ArrayList<Double>> temposDeEstado = new ArrayList<>();
		for(int i=0; i<estruturaFila.size(); i++) {
			temposDeEstado.add(new ArrayList<Double>());
			idsFila[i] = estruturaFila.get(i).id;
		}
		SimulationReport res = new SimulationReport(idsFila, 0.0, temposDeEstado, new double[estruturaFila.size()]);
		
		
		for(long r : seeds) {
			SimulationReport tempo = runSimulation(r, alets);
			res.sumSimulation(tempo);
		}
		/* Divide the accumulated results by the number of simulations run to obtain
		 * the average of the results. */
		res.averageResults(seeds.size());
		System.out.printf("Printing average results of %d simulations:\n", seeds.size());
		System.out.println(res.toString());
	}
	
	private SimulationReport runSimulation(long aletseed, int totalalets) {
		
		//Event schedule
		Comparator<ScheduleEntry> schComparator = new ScheduleEntry();
		PriorityQueue<ScheduleEntry> schedule = new PriorityQueue<>(schComparator);
		//First arrivals are offered to the schedule
        for(ScheduleEntry se : primChegada) {
        	schedule.offer(se);
        }
               
        RNG rng = new RNG(aletseed);
        double time = 0;
        
        while(totalalets > 0) {
            
            //Process next scheduled event
            ScheduleEntry se = schedule.poll();
            double variacaoTempo = se.time-time;
            for(QueueStructure q : estruturaFila) {
            	q.updateQueueTimes(variacaoTempo);
            }
            time += variacaoTempo; //update simulation clock
            
            
            if(se.event == EventEnum.ARRIVAL) {
                QueueStructure dest = se.destino;
            	if(!dest.isFull()) { //Queue can receive the client
                    dest.addClient();
                    if(dest.canServeOnArrival()) { //Queue can serve the client
                        totalalets -= scheduleDeparture(schedule, dest, time, rng);
                    }
                } else { //Queue full
                    dest.clientsLost++;
                }
                scheduleArrival(schedule, dest, time, rng);
                totalalets--;
            
            
            
            } else if(se.event == EventEnum.PASSAGE) {
            	QueueStructure ori = se.origin;
            	QueueStructure dest = se.destino;
            	ori.removeClient();
                if(ori.canServeOnDeparture()) { //Origin can serve another client.
                	totalalets -= scheduleDeparture(schedule, ori, time, rng);
                }
                if(!dest.isFull()) { //destino can take another client.
                	dest.addClient();
                	if(dest.canServeOnArrival()) { //destino can serve another client.
                		totalalets -= scheduleDeparture(schedule, dest, time, rng);
                	}
            	} else { //destino full. Client lost.
            		dest.clientsLost++;
            	}
            
            
                
            } else { //It's a departure
            	QueueStructure ori = se.origin;
            	ori.removeClient();
            	if(ori.canServeOnDeparture()) { //Can serve one more client
            		totalalets -= scheduleDeparture(schedule, ori, time, rng);
            	}
            }
        }
        
        //Simulation finished. Make report.
        ArrayList<ArrayList<Double>> qTimes = new ArrayList<>();
        double[] clientsLost = new double[estruturaFila.size()];
        String[] idsFila = new String[estruturaFila.size()];
        for(int i=0; i<estruturaFila.size(); i++) {
        	idsFila[i] = estruturaFila.get(i).id;
        	qTimes.add(estruturaFila.get(i).temposDeEstado);
        	clientsLost[i] = estruturaFila.get(i).clientsLost;
        }
        
        SimulationReport sr = new SimulationReport(idsFila, time, qTimes, clientsLost);
        
        //Reset the state of all queues.
        for(QueueStructure q : estruturaFila) {
        	q.resetSimulationVariables();
        }
        
        return sr;
	}
	
	private void scheduleArrival(PriorityQueue<ScheduleEntry> schedule, QueueStructure destino, double time, RNG rng) {
		double randomNumber = rng.next();
		double eventTime = time + (destino.arrivalMax-destino.arrivalMin) * randomNumber + destino.arrivalMin;
		schedule.offer(ScheduleEntry.newArrival(eventTime, destino));
	}
	
	private int scheduleDeparture(PriorityQueue<ScheduleEntry> schedule, QueueStructure origin, double time, RNG rng) {
		//Define event time
		double randomNumber = rng.next();
		int aletsUsed = 1;
		double eventTime = time + (origin.serviceMax-origin.serviceMin) * randomNumber + origin.serviceMin;
		
		QueueStructure dest = null;
		/* If more than one possible destino, roll the probabilities.
		 * This consumes an extra random number. */
		if(origin.destinos.size()>1) {
			double randomProb = rng.next();
			aletsUsed++;
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
		
		//Generate schedule events accordingly
		if(dest == QueueStructure.EXIT) {//Departure from the system
			schedule.offer(ScheduleEntry.newDeparture(eventTime, origin));
		} else { //Passage from one queue to another
			schedule.offer(ScheduleEntry.newPassage(eventTime, origin, dest));
		}
		
		return aletsUsed;
	}
	
	
	
	
	
	
	//===========================================================
	// File parsing methods
    //===========================================================
	
	private void parseFile(String nomeArquivo) throws Exception {
		try {
			estruturaFila = new ArrayList<>();
			seeds = new ArrayList<>();
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
						int beginning = s.indexOf(':');
						definedestino(estruturaFila, s.substring(beginning+1));
						
					} else if(s.charAt(0)=='s') { //Line defines seeds for the rng
						int beginning = s.indexOf(':');
						defineSeeds(s.substring(beginning+1));
					
					} else if(s.charAt(0)=='r') { //Line defines the amount of alets to be used
						int beginning = s.indexOf(':');
						alets = Integer.parseInt(s.substring(beginning+1).trim());
						
					} else if(s.charAt(0)=='f') { //Line defines the first arrivals for the queues
						int beginning = s.indexOf(':');
						defineprimChegada(s.substring(beginning+1));
						
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
	
	private QueueStructure createQueue(String s) throws Exception {
		
			String[] splitOnColon = s.replaceAll("\\s", "").split(":");
			String[] params = splitOnColon[1].split("/");
			int capacity;
			if(params[1].equals("inf")) {
				capacity = Integer.MAX_VALUE;
			} else {
				capacity = Integer.parseInt(params[1]);
			}
			return new QueueStructure(
					splitOnColon[0], //id
					Integer.parseInt(params[0]), //servers
					capacity,
					Double.parseDouble(params[2]), //arrivalMin
					Double.parseDouble(params[3]), //arrivalMax
					Double.parseDouble(params[4]), //serviceMin					
					Double.parseDouble(params[5]), //serviceMax
					null); //destinos
	}
	
	private void definedestino(ArrayList<QueueStructure> estruturaFila, String s) throws Exception {
		String[] connectedestruturaFila = s.split("(->)");
		
		//Defines the origin queue whose destinos are being parsed
		String originName = connectedestruturaFila[0].trim();
		QueueStructure origin = findQueue(estruturaFila, originName);
		
		//Parse destinos
		String[] destinos = connectedestruturaFila[1].split(",");
		//Add each destino with it's corresponding routing probability to the origin object
		for(String d : destinos) {
			QueueStructure dest = null;
			String[] destAndProb = d.split("/");
			String destName = destAndProb[0].trim();
			if(destName.equals("S") || destName.equals("s")) { //destino is the system exit
				dest = QueueStructure.EXIT;
			} else { //destino is one of the system's queues
				dest = findQueue(estruturaFila, destName);
			}
			origin.destinos.add(dest);
			origin.probabilidadeDestino.add(Double.parseDouble(destAndProb[1]));
		}
		double sum = 0.0;
		for(double p : origin.probabilidadeDestino) {
			sum += p;
		}
		if(sum != 1.0) {
			throw new Exception("The sum of all routing probabilities in a queue must equal 1.");
		}
	}
	
	private QueueStructure findQueue(ArrayList<QueueStructure> estruturaFila, String name) throws Exception{
		for(QueueStructure q : estruturaFila) {
			if(q.id.equals(name)) return q;
		}
		throw new Exception(String.format(
				"Queue \"%s\" does not exist or wasn't previously defined in input file.", name));
		
	}
	
	private void defineSeeds(String str) {
		String[] longsString = str.replaceAll("\\s", "").split(",");
		for(String s : longsString) {
			seeds.add(Long.parseLong(s));
		}
	}
	
	private void defineprimChegada(String str) throws Exception{
		for(String s1 : str.replaceAll("\\s", "").split(",")) {
			String[] queueAndArr = s1.split("/");
			double time = Double.parseDouble(queueAndArr[1]);
			QueueStructure q = findQueue(estruturaFila, queueAndArr[0]);
			primChegada.add(ScheduleEntry.newArrival(time, q));
		}
	}
}


















