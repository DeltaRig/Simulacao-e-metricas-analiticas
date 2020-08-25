
/**
 * Objetivo: Utilizando o conceito do método Congruente Linear para geração de
 * números pseudo-aleatórios, a classe apresenta um método que gera 1.000 (mil)
 * números (entre os valores 0 e 1) a fim de produzir um gráfico de dispersão
 * 
 * @author (Daniela Pereira Rigoli)
 * @version (13/08/2020) 
 * Professor: Afonso Henrique Correa de Sales
 * Cadeira: Simulação e Métodos Analíticos
 * 
 * Método pseudoAleatorio: Gera 1000 números pseudo-aleatórios e já escreve no arquivo.
 * 
 */
package MediasNum;

import java.io.*;

 public class App {
    public static void main(String args[]) throws IOException {
        double random1 = 31.98401;
        double random2 = 4.50918;
        double random3 = 355-948;
        double random4 = 130143;
        double random5 = 939.413;
        double media = 0;


        System.out.println("seed1: " + random1);
        System.out.println("seed2: " + random2);
        System.out.println("seed3: " + random3);
        System.out.println("seed4: " + random4);
        System.out.println("seed5: " + random5);
        
  


        FileWriter ea = new FileWriter("C:/Users/hitom/Documents/ProjetosGITs/SMA/MediasNum/MediaGerada.yml"); //Completar o path
     
        ea.write("!PARAMETERS\nrndnumbers:\n");


        for(int i = 0; i < 1000; i++){
            random1 = pseudoAleatorio(random1);
            random2 = pseudoAleatorio(random2);
            random3 = pseudoAleatorio(random3);
            random4 = pseudoAleatorio(random4);
            random5 = pseudoAleatorio(random5);

            media = (random1 + random2 + random3 + random4 + random5)/5;

            ea.write(media + "\n");

        }
        ea.close();
    }


    public static double pseudoAleatorio(double random){
    double euler = Math.E;

    random += Math.pow(random, euler) + Math.pow(2, Math.PI) + 0.31;
    random = random * 10;
    random = random - Math.floor(random);
    return random;
    
    }
        

}