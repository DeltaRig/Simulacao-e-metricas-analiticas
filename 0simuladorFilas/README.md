Desenvolvedoras: Daniela Rigoli e Franciele Constante
Turma: 128
Professor: Afonso Sales

# Trabalho 1 - Parte 1

### Descrição:
O programa lê um arquivo com variáveis definidas e simulação de acordo com as especificações descritas no arquivo.

### Como rodars
Utilizando os comandos abaixo você poderá rodar o programa.
>javac *.java

>java App < nomeDoArquivo >

Há a possibilidade de ler um arquivo diferente passando o nome deste arquivo pela linha de comando. No arquivo deve ser informado o valor da primeira chegada e as caracteriscas de cada fila devem aparecer obrigatóriamente para gerar uma simulação. Se não for informado nenhum nome de arquivo o programa irá ler o conteúdo do arquivo "filas".

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
