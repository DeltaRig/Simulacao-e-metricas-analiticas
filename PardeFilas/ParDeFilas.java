/**
 * Simulador de par de fila (sem roteamento)
 * 
 * Andamento:
 *      Variaveis criadas
 *      Deterministrico alterado
 *      Falta: 
 *          Arrumar o processo aleatório
 *          O que acontece se quando a 2º pessoa chega na fila1
 *          Adicionar verificações para a rotação da fila2
 * 
 * @author (Daniela Pereira Rigoli)
 * @version (13/08/2020) 
 * Professor: Afonso Henrique Correa de Sales
 * Cadeira: Simulação e Métodos Analíticos
 * 
 * 
 */

package PardeFilas;

public class ParDeFilas{
    public static void main(String[] args) {
        int operacao = 1;
        
        double tempo = 0;
        double saida = 2;

        double[] rndnumbers = {0.9921, 0.0004, 0.5534, 0.2761, 0.3398, 0.8963, 0.9023, 0.0132, 0.4569, 0.5121, 0.9208, 0.0171, 0.2299, 0.8545, 0.6001, 0.2921};
        int i = 0;
        int perda = 0;
              
    
        //caracteristicas fila1:
        Processo[] fila1 = new Processo[1];
        int capacidade1 = 3;
        int servidores1 = 2;
        double proxSaidaS1; //devido a quantidade de servidores
        double proxSaidaS2;

        double minArrival1 = 2;
        double maxArrival1 = 3;
        double minService1 = 2;
        double maxService1 = 5;

        //caracteristicas fila2:
        Processo[] fila2 = new Processo[0];
        final int servidores2 = 1;
        double proxSaida;
        int capacidade2 = 3;
        double minService2 = 3;
        double maxService2 = 5.0;
  
        //para começar foi determinado
        // agendo a primeira chegada NO LOCAL
        double chegada = 2;
        double chegaProx = chegada + tempo;
        rndnumbers[i] = pseudoAleatorio(rndnumbers[i]);
        if(i < rndnumbers.length){
            i++;
        } else {
            i = 0;
        }

        tempo = chegada;
        Processo p = new Processo(chegada, saida, tempo);
        fila1[0] = p;

        System.out.println("Operacao: " + operacao + 
            "\nCHEGADA no minuto: " + (chegada));
        operacao++;
        

        //processos aleatórios
        while(tempo <= 8.1){

            //chegada
            if(tempo >= chegaProx){
                
                if(fila1.length < capacidade1){ //fila1
                    System.out.println("Operacao: " + operacao + 
                        "\ntempo para chegar: " + chegada + 
                        "\nCHEGADA no minuto: " + chegaProx);
                    operacao++;

                    Processo pr = new Processo(chegada, saida, chegaProx); //saida
                    saida  = (maxService1 - minService1) * pseudoAleatorio(rndnumbers[i]) + minService1;
                    rndnumbers[i] = pseudoAleatorio(rndnumbers[i]);
                    if(i < rndnumbers.length - 1){
                        i++;
                    } else {
                        i = 0;
                    }

                    //adiciona p no array
                    adicionaFila(fila1, pr);
                } else {
                    perda++;
                }    

                //agenda proxima chegada
                chegada = (maxArrival1 - minArrival1) * pseudoAleatorio(rndnumbers[i]) + minArrival1;
                chegaProx = chegada + tempo;
                rndnumbers[i] = pseudoAleatorio(rndnumbers[i]);
                if(i < rndnumbers.length - 1){
                    i++;
                } else {
                    i = 0;
                }
            }

            for(i = 1; i <= servidores1; i++){ // começa em 1 pois se tiver 0 servidores ninguém será atendido
                if(tempo >= fila1[i - 1].getSaida() + fila1[i - 1].getTempo()){
                    System.out.println("Operacao: " + operacao + 
                    "\ntempo para atendimento: " + fila1[i - 1].getSaida() + 
                    "\nSAIDA no minuto: " + fila1[i - 1].getSaida() + fila1[i - 1].getTempo());
                    operacao++;
                
                    //remove 1
                    remove(fila1, i);
                    //agenda proxima saída
                    fila1[i - 1].setTempo(tempo);
                }
            }
            
            
            tempo = tempo + 0.1; //igual ao valor da fila
            
        }
        System.out.println("Total de perdas: " + perda);
        System.out.println("Tempo total: " + tempo);
        System.out.println("Fila terminou com: " + fila1.length + "pessoas");
            
        

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
                if(x != posicao){
                    filaAtualiza[cont] = fila[x];
                    cont++;
                }
                
            }
            return filaAtualiza;
    }

}