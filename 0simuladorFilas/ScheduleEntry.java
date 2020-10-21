import java.util.Comparator;

/* Used to schedule events during simulation. */
public class ScheduleEntry implements Comparator<ScheduleEntry> {
    
	TipoEvento evento;
    double tempo;
    QueueStructure origin;
	QueueStructure destino;
	
	/* Arrivals require destino != null
	 * Departures require origin != null
	 * Passages require both origin and destino != null */
	public ScheduleEntry(TipoEvento evento, double tempo, QueueStructure origin, QueueStructure destino) {
		this.evento = evento;
		this.tempo = tempo;
		this.origin = origin;
		this.destino = destino;
	}
	
	public ScheduleEntry() {
		
	}
	
	public static ScheduleEntry newArrival(double tempo, QueueStructure destino) {
		return new ScheduleEntry(TipoEvento.CHEGADA, tempo, null, destino);
	}
	public static ScheduleEntry newDeparture(double tempo, QueueStructure origin) {
		return new ScheduleEntry(TipoEvento.SAIDA, tempo, origin, QueueStructure.EXIT);
	}
	public static ScheduleEntry newPassage(double tempo, QueueStructure origin, QueueStructure destino) {
		return new ScheduleEntry(TipoEvento.PASSAGEM, tempo, origin, destino);
	}
	
	public int compare(ScheduleEntry se1, ScheduleEntry se2) {
        if(se1.tempo < se2.tempo)
            return -1;
        if(se1.tempo > se2.tempo)
            return 1;
        return 0;
    }
	
	public ScheduleEntry clone() {
		return new ScheduleEntry(evento, tempo, origin, destino);
	}
}
//===========================================================