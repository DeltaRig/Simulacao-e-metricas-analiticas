import java.util.Comparator;

/* Used to schedule events during simulation. */
public class Escalonador implements Comparator<Escalonador> {
    
	TipoEvento evento;
    double tempo;
    Fila origem;
	Fila destino;
	
	/* Arrivals require destino != null
	 * Departures require origem != null
	 * Passages require both origem and destino != null */
	public Escalonador(TipoEvento evento, double tempo, Fila origem, Fila destino) {
		this.evento = evento;
		this.tempo = tempo;
		this.origem = origem;
		this.destino = destino;
	}
	
	public Escalonador(){}
	
	public static Escalonador newArrival(double tempo, Fila destino) {
		return new Escalonador(TipoEvento.CHEGADA, tempo, null, destino);
	}
	public static Escalonador newDeparture(double tempo, Fila origem) {
		return new Escalonador(TipoEvento.SAIDA, tempo, origem, Fila.FIM);
	}
	public static Escalonador newPassage(double tempo, Fila origem, Fila destino) {
		return new Escalonador(TipoEvento.PASSAGEM, tempo, origem, destino);
	}
	
	public int compare(Escalonador se1, Escalonador se2) {
        if(se1.tempo < se2.tempo)
            return -1;
        if(se1.tempo > se2.tempo)
            return 1;
        return 0;
    }
	
	public Escalonador clone() {
		return new Escalonador(evento, tempo, origem, destino);
	}
}
