/**
 * Objetivo: Utilizando o conceito do método Congruente Linear para geração de
 * números pseudo-aleatórios, a classe apresenta um método que gera 1.000 (mil)
 * números (entre os valores 0 e 1) a fim de produzir um gráfico de dispersão
 * 
 * @author (Daniela Pereira Rigoli)
 * @version (13/08/2020) Professor: Cadeira: Simulação e Métodos Analíticos
 */
package FimGeradorNumeros;

import java.io.*;

 public class App {
    public static void main(String args[]) throws IOException {
        pseudoAleatorio(0.3276);
    }

    public static void pseudoAleatorio(double random) throws IOException {
        FileWriter ea = new FileWriter(".../numeros.txt"); //Completar o path

        double euler = Math.E;
        boolean segue = true;
        long i = 0;

        do{
            i++;
            random += Math.pow(random, euler) + Math.pow(2, Math.PI) + 0.31;
            random = random * 10;
            random = random - Math.floor(random);

            
            System.out.println(random);
            segue = valida(random);
            ea.write(random + "\n");
        }while(segue);

        ea.write("O ciclo reinicia na posição: " + i);
        ea.close();

        System.out.println("O ciclo reinicia na posição: " + i);
    }

    public static boolean valida(double num) throws IOException {
        FileInputStream stream = new FileInputStream("C:/Users/hitom/Documents/ProjetosGITs/SMA/FimGeradorNumeros/numeros.txt");
        InputStreamReader reader = new InputStreamReader(stream);
     
        String compara = String.valueOf(num);
        BufferedReader br = new BufferedReader(reader);
        String linha = br.readLine();
        long i = 0;
        while(linha != null) {
            i++;
            if(linha.equals(compara)){
                System.out.println(linha + " é igual a " + compara + "\nlinha: " + i);
                return false;
            }
            linha = br.readLine();
        }
        br.close();
        return true;
    }

}