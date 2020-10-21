import java.util.Comparator;

/* Used to schedule events during simulation. */
public class Escalonador implements Comparator<Escalonador> {
    
	TipoEvento evento;
    double tempo;
    QueueStructure origin;
	QueueStructure destino;
	
	/* Arrivals require destino != null
	 * Departures require origin != null
	 * Passages require both origin and destino != null */
	public Escalonador(TipoEvento evento, double tempo, QueueStructure origin, QueueStructure destino) {
		this.evento = evento;
		this.tempo = tempo;
		this.origin = origin;
		this.destino = destino;
	}
	
	public Escalonador(){}
	
	public static Escalonador newArrival(double tempo, QueueStructure destino) {
		return new Escalonador(TipoEvento.CHEGADA, tempo, null, destino);
	}
	public static Escalonador newDeparture(double tempo, QueueStructure origin) {
		return new Escalonador(TipoEvento.SAIDA, tempo, origin, QueueStructure.FIM);
	}
	public static Escalonador newPassage(double tempo, QueueStructure origin, QueueStructure destino) {
		return new Escalonador(TipoEvento.PASSAGEM, tempo, origin, destino);
	}
	
	public int compare(Escalonador se1, Escalonador se2) {
        if(se1.tempo < se2.tempo)
            return -1;
        if(se1.tempo > se2.tempo)
            return 1;
        return 0;
    }
	
	public Escalonador clone() {
		return new Escalonador(evento, tempo, origin, destino);
	}
}
