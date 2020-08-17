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
package GeradorNumeros;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

 public class App {
    public static void main(String args[]) throws IOException {
        Calendar lCDateTime = Calendar.getInstance();
  
        pseudoAleatorio(lCDateTime.getTimeInMillis()); //usamos os milisegundos para que os números pseudo-aleatórios não iniciem sempre iguais
    }

    public static void pseudoAleatorio(double random) throws IOException {
        FileWriter ea = new FileWriter(".../numeros.txt"); //Completar o path
     
        double euler = Math.E;

        for(int i = 0; i < 1000; i++){
            random += Math.pow(random, euler) + Math.pow(2, Math.PI) + 0.31;
            random = random * 10;
            random = random - Math.floor(random);
            ea.write(random + "\n");
        }
        ea.close();
    }

}