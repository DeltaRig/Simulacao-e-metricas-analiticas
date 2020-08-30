/**
 * Simulador de fila simples
 * 
 * @author (Daniela Pereira Rigoli)
 * @version (25/08/2020) 
 * Professor: Afonso Henrique Correa de Sales
 * Cadeira: Simulação e Métodos Analíticos
 * 
 * 
 */

package T1;

import java.io.*;
import java.util.*;

public class App {
    public static void main(String args[]){
    Scanner scan = new Scanner(System.in);

    int operation = 1;
    int i = 0;
    double time = 0;
    double minArrival = 1;
    double maxArrival = 2;
    double arrivals = 2;

        
        boolean verifica = false;
        do{ 
            verifica = false;
            System.out.println("Digite o caminho para o arquivo (com o nome do arquivo)"
                    + "\nEx.: C:/Users/(nome)/Documents/arquivo.txt");
        
            String path = scan.nextLine();
            
            try(BufferedReader reader = new BufferedReader(new FileReader(path));){
                // ler o arquivo e enviar para a tradução
                for (String linha = reader.readLine(); linha != null; linha = reader.readLine()){
                    if(linha.length() > 0){

                        if(linha.equals("!PARAMETERS")){ //começa a reconhecer as variaveis

                        }

                    }
                }
                reader.close();
                verifica = true;
            } catch (IOException e) {
                System.out.println("O arquivo não foi encontrado");
            }
        }while(verifica == false); // Para quando o arquivo for encontrado


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