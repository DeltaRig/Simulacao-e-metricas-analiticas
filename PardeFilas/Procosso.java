package PardeFilas;

class Processo {
    private double chegada;
    private double saida;
    private double tempo;
    //private double atendimento;

    public Processo(double chegada, double saida, double tempo){
        this.chegada = chegada;
        this.saida = saida;
        this.tempo = tempo;
    }
    //getters
    public double getChegada(){
        return this.chegada;
    }
    public double getSaida(){
        return this.saida;
    }
    public double getTempo(){
        return this.tempo;
    }

    /*
    public double getAtendimento(){
        return this.atendimento;
    }
    //setters
    
    public void setAtendimento(double atendimento){
        this.atendimento = atendimento;
    }
    */
}


