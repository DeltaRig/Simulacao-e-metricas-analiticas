Desenvolvedoras: Daniela Rigoli e Franciele Constante

# Trabalho 1 - Parte 1

### Descrição:
Simula uma rede filas recendo as variaveis de um arquivo.

Para usar o simulador compilar e rodar o programa App

>javac *.java
>java App

O programa vai ler o sistema de filas no arquivo "filas" e simular de acordo com as especificações de simulação contidas naquele arquivo.

É possível ler um aruivo diferente passando o nome do arquivo por linha de comando

>java App filas2

O arquivo "filas" está comentado e ensina como funciona a sintaxe para definir uma rede de filas. O arquivo "filas2" possui um segundo exemplo de rede de filas.

A principal classe de simulação é a QueueSim, contendo quase toda a lógica de simulação.

Objetos da classe Fila guardam a especificação de uma fila.

A classe Escalonador é usada pelo simulador para representar eventos escalonados.

A classe SimulationReport é usada pelo simulador para guardar os resultados de uma simulação.

RNG é a classe do gerador de números aleatórios.

### Classes:
- App: Verifica se foi informado um arquivo e encaminho para o simulador.
- QueueSim: >> Simulador
- Fila: Armazena todas os dados da fila.
- GeradorNumerosAleatorios: Gera números aleátorios ou utilizar valores pré definidos para as operações.
- Escalonador: Gerencia os eventos do simulador, armazenando se o informando se são de saída ou chegada (o que é definido no momento de seu agendamento) e o momento em que deve ocorrer. Além de informar de onde para onde a pessoa vai.
- SimulationReport: >> Resultados
- TipoEvento: Enum com os tipos de evento.

### Referências:
- https://aaronschlegel.me/linear-congruential-generator-r.html
