/**
 * Simulador de fila simples
 * 
 * @author (Daniela Pereira Rigoli)
 * @version (13/08/2020) 
 * Professor: Afonso Henrique Correa de Sales
 * Cadeira: Simulação e Métodos Analíticos
 * 
 * 
 */

package FilaSimples;

public class FilaSimples{
    public static void main(final String[] args) {
        int operacao = 1;
        int i = 0;
        double tempo = 0;
        final double minArrival = 1;
        final double maxArrival = 2;
        final double minService = 3;
        final double maxService = 6;
        final int capacidade = 3;
        final int servidores = 1;

        double[] rndnumbers = {0.3276, 0.8851, 0.1643, 0.5542, 0.6813, 0.7221, 0.9881};
        int perda = 0;
              
        Processo[] fila = new Processo[0];

        //agendo a saida
        double chegada = 2;
        double saida  = (maxService - minService) * pseudoAleatorio(rndnumbers[i]) + minService;
        double chegaProx = chegada + tempo;
        rndnumbers[i] = pseudoAleatorio(rndnumbers[i]);
        if(i < rndnumbers.length){
            i++;
        } else {
            i = 0;
        }
        tempo = chegada;
        Processo p = new Processo(chegada, saida, tempo);
        fila = adicionaFila(fila, p);

        System.out.println("Operacao: " + operacao + 
        "\ntempo para chegar: " + chegada + 
        "\nCHEGADA no minuto: " + chegaProx);
        operacao++;
            

        // agendo a proxima chegada
        chegada = (maxArrival - minArrival) * pseudoAleatorio(rndnumbers[i]) + minArrival;
        chegaProx = chegada + tempo;
        rndnumbers[i] = pseudoAleatorio(rndnumbers[i]);
        if(i < rndnumbers.length){
            i++;
        } else {
            i = 0;
        }
        
        while(tempo <= 8.1){

            //chegada
            if(tempo >= chegaProx){
                
                if(fila.length < capacidade){ //fila
                    System.out.println("Operacao: " + operacao + 
                        "\ntempo para chegar: " + chegada + 
                        "\nCHEGADA no minuto: " + chegaProx);
                    operacao++;

                    
                    saida  = (maxService - minService) * pseudoAleatorio(rndnumbers[i]) + minService;
                    Processo pr = new Processo(chegada, saida, chegaProx); //saida
                    rndnumbers[i] = pseudoAleatorio(rndnumbers[i]);
                    if(i < rndnumbers.length - 1){
                        i++;
                    } else {
                        i = 0;
                    }

                    //adiciona p no array
                    adicionaFila(fila, pr);
                } else {
                    perda++;
                }    

                //agenda proxima chegada
                chegada = (maxArrival - minArrival) * pseudoAleatorio(rndnumbers[i]) + minArrival;
                chegaProx = chegada + tempo;
                rndnumbers[i] = pseudoAleatorio(rndnumbers[i]);
                if(i < rndnumbers.length - 1){
                    i++;
                } else {
                    i = 0;
                }
            }

            for(i = 1; i <= servidores; i++){ // começa em 1 pois se tiver 0 servidores ninguém será atendido
                if(fila.length <= i){ //ALERTA
                    if(tempo >= (fila[i - 1].getSaida() + fila[i - 1].getTempo())){
                        System.out.println("Operacao: " + operacao + 
                        "\ntempo para atendimento: " + fila[i - 1].getSaida() + 
                        "\nSAIDA no minuto: " + fila[i - 1].getSaida() + fila[i - 1].getTempo());
                        operacao++;
                    
                        //remove 1
                        remove(fila, i);
                        //agenda proxima saída
                        fila[i - 1].setTempo(tempo);
                    }
                }
            }
            
            
            tempo = tempo + 0.1; //igual ao valor da fila
            
        }
        System.out.println("Total de perdas: " + perda);
        System.out.println("Tempo total: " + tempo);
        System.out.println("Fila terminou com: " + fila.length + " pessoas");
            
        

    }  

    public static double pseudoAleatorio(double random){
        double euler = Math.E;

        random += Math.pow(random, euler) + Math.pow(2, Math.PI) + 0.31;
        random = random * 10;
        random = random - Math.floor(random);
        return random;
        
    }

    public static Processo[] adicionaFila(Processo[] fila, Processo pr){
        Processo[] filaAtualiza = new Processo[fila.length + 1];
            for(int x = 0; x < fila.length; x++){
                filaAtualiza[x] = fila[x];
            }
            filaAtualiza[fila.length] = pr;
            return filaAtualiza;
    }

    public static Processo[] remove(Processo[] fila, int posicao){
        Processo[] filaAtualiza = new Processo[fila.length - 1];
        int cont = 0;
            for(int x = 0; x < fila.length; x++){
                if(cont < filaAtualiza.length){
                    if(x != posicao){
                        filaAtualiza[cont] = fila[x];
                        cont++;
                    }
                }                
            }
            return filaAtualiza;
    }

}