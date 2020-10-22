Desenvolvedoras: Daniela Rigoli e Franciele Constante

# Trabalho 1 - Parte 1

### Descrição:
O programa lê um arquivo com variáveis definidas e simulação de acordo com as especificações descritas no arquivo.

### Para rodar
Para rodar o simulador utilizar os comandos abaixo:

>javac *.java




>java App < nomeDoArquivo >

É possível ler um arquivo diferente passando o nome do arquivo por linha de comando. O arquivo deve informar o valor da primeira chegada e as caracteriscas de cada fila obrigatóriamente para gerar uma simulação.


















O arquivo "filas" está comentado e ensina como funciona a sintaxe para definir uma rede de filas.

### Classes:
- App: Verifica se foi informado um arquivo e encaminho para o simulador.
- Simulador: Possui a lógica da simulação.
- Fila: Armazena todas os dados da fila.
- GeradorNumerosAleatorios: Gera números aleátorios ou utilizar valores pré definidos para as operações.
- Escalonador: Gerencia os eventos do simulador, armazenando e informando se são de saída ou chegada (o que é definido no momento de seu agendamento) e o momento em que deve ocorrer. Além de informar de onde para onde a pessoa vai.
- Resultados: Utilizado para guardar os resultados de uma simulação.
- TipoEvento: Enum com os tipos de evento.

### Referências:
- https://aaronschlegel.me/linear-congruential-generator-r.html
