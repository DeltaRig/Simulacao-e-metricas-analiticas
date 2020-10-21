public class App {

	public static void main(String[] args) throws Exception {
		
		String nomeArquivo;
		if(args.length > 0) {
			nomeArquivo = args[0];
		} else {
			nomeArquivo = "filas";
		}
		
		QueueSim simulador = new QueueSim(nomeArquivo);
		simulador.runSimulation();
		
	}

}





























