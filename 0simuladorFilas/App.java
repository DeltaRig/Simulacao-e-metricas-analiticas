public class App {

	public static void main(String[] args) throws Exception {
		
		String fileString;
		if(args.length > 0) {
			fileString = args[0];
		} else {
			fileString = "filas";
		}
		
		QueueSim sim = new QueueSim(fileString);
		sim.runSimulation();
		
	}

}






























