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
    public static void main(String[] args) {
        int operacao = 1;
        int i = 0;
        double tempo = 0;
        double minArrival = 1;
        double maxArrival = 2;
        double minService = 3;
        double maxService = 6;
        double[] rndnumbers = {0.3276, 0.8851, 0.1643, 0.5542, 0.6813, 0.7221, 0.9881};
        int perda = 0;
              
        Processo[] fila = new Processo[1];

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
        double proxSaida = saida + tempo;
        Processo p = new Processo(chegada, saida, tempo);
        fila[0] = p;

        System.out.println("Operacao: " + operacao + 
            "\nCHEGADA no minuto: " + (chegada));
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
                
                if(fila.length < 3){
                    Processo pr = new Processo(chegada, saida, chegaProx); //saida
                    saida  = (maxService - minService) * pseudoAleatorio(rndnumbers[i]) + minService;
                    rndnumbers[i] = pseudoAleatorio(rndnumbers[i]);
                    if(i < rndnumbers.length - 1){
                        i++;
                    } else {
                        i = 0;
                    }
                    System.out.println("Operacao: " + operacao + 
                        "\ntempo para chegar: " + chegada + 
                        "\nCHEGADA no minuto: " + chegaProx);
                    operacao++;

                    //adiciona p no array
                    Processo[] filaAtualiza = new Processo[fila.length + 1];
                    for(int x = 0; x < fila.length; x++){
                        filaAtualiza[x] = fila[x];
                    }
                    filaAtualiza[fila.length] = pr;
                    fila = filaAtualiza;
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

            if(tempo >= proxSaida){
                System.out.println("Operacao: " + operacao + 
                    "\ntempo para atendimento: " + fila[0].getSaida() + 
                    "\nSAIDA no minuto: " + proxSaida);
                operacao++;
                
                    //remove 1
                Processo[] filaAtualiza = new Processo[fila.length - 1];
                for(int x = 0; x < filaAtualiza.length; x++){
                    filaAtualiza[x] = fila[x + 1];
                }
                fila = filaAtualiza;
                //agenda proxima saída
                proxSaida = fila[0].getSaida() + tempo;
            }
            
            tempo = tempo + 0.1; //igual ao valor da fila
            
        }
        System.out.println("Total de perdas: " + perda);
        System.out.println("Tempo total: " + tempo);
        System.out.println("Fila terminou com: " + fila.length + "pessoas");
            
        

    }

    

    public static double pseudoAleatorio(double random){
        double euler = Math.E;

        random += Math.pow(random, euler) + Math.pow(2, Math.PI) + 0.31;
        random = random * 10;
        random = random - Math.floor(random);
        return random;
        
    }

}