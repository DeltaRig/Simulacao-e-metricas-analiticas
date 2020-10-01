import java.util.ArrayList;

public class Escalonador {
    private ArrayList<Evento> eventos;

    public Escalonador(){
        eventos = new ArrayList<Evento>();
    }

    public void addEvento(Evento evento) {
        eventos.add(evento);
    }

    //remove eventos

    public ArrayList<Evento> getEventos() {
        return eventos;
    }

    //proximoEvento
}
