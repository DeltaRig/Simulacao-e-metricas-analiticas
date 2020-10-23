import java.util.Comparator;

//Faz o agendamento dos eventos durante a simulação
public class Escalonador implements Comparator<Escalonador> {
    
	TipoEvento evento;
    double tempo;
    Fila origem;
	Fila destino;
	
	//Chegadas exigem destino != null
	//Saídas exigem origem != null
	//Passagens exigem origem e destino != null
	public Escalonador(TipoEvento evento, double tempo, Fila origem, Fila destino) {
		this.evento = evento;
		this.tempo = tempo;
		this.origem = origem;
		this.destino = destino;
	}

	public Escalonador(){}
	
	public static Escalonador chegada(double tempo, Fila destino) {
		return new Escalonador(TipoEvento.CHEGADA, tempo, null, destino);
	}
	public static Escalonador saida(double tempo, Fila origem) {
		return new Escalonador(TipoEvento.SAIDA, tempo, origem, Fila.FIM);
	}
	public static Escalonador passagem(double tempo, Fila origem, Fila destino) {
		return new Escalonador(TipoEvento.PASSAGEM, tempo, origem, destino);
	}
	
	public int compare(Escalonador esc, Escalonador outroEsc) {
        if(esc.tempo < outroEsc.tempo)
            return -1;
        if(esc.tempo > outroEsc.tempo)
            return 1;
        return 0;
    }
	
	public Escalonador clone() {
		return new Escalonador(evento, tempo, origem, destino);
	}
}
