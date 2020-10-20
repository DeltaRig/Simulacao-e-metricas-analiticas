Desenvolvedoras: Daniela Rigoli e Franciele Constante

# Trabalho 1 - Parte 1

### Descrição:
Nessa primeira versão o projeto contém um gerador de números aleatórios e executa a simulação de uma fila simples recebendo os dados passados pelo usuário.

## Informações de execução:
Foi implementado em Java 14.
Funciona apenas para fila simples, recebe o caminho que você escrever quando for solicitado e encontra o arquivo se ele existir.

## Sobre o arquivo de entrada:
- É obrigatório fornecer sementes ou números aleatórios para o programa se basear, basta seguir o exemplo do arquivo "entrada.txt", se quiser usar sementes basta mudar a palavra "rndnumbers" para "seeds" (É possível ver um arquivo de entrada com esse exemplo em "entradaComSeeds.txt").
- Se for utilizado sementes a quantidade de operações será igual a 100000.
- É obrigatório infomrar o tempo da primeira chegada.
- É obrigatório informar a quantidade de servidores, a capacidade, o tempo mínimo de chegada, o tempo máximo de chegada, o tempo mínimo de serviço e o tempo máximo de serviço nessa ordem como mostra no exemplo "entrada.txt".

### Classes:
- Evento: Armazena o tipo (se é de chagada ou saída) e o tempo desse evento;
- Fila: Guarda os dados individuais de cada fila e faz o gerenciamento dos métodos relacionados a mesma (adição de pessoa, remoção, estado da fila e gets utilizados);
- GeradorNumerosAleatórios: Atualmente usado para gerar n números aleátorios ou utilizar valores pré definidos para as operações.
- Resultados: Criado para facilitar a geração da média de _n_ simulações, armazena o resultado de cada uma das simulações concluidas.
- Simulador: Atual classe principal. Na main organaniza quantas simulações devem ser feitas e quais os dados utilzados além de mostrar o resultado final. No método filaSimples executa todas as funções necessárias da simulação.

- Escalonador: Irá gerenciar os eventos do simulador (não implementado atualmente), porém existe uma lista que armazena os eventos informando se são de saída ou chegada (o que é definido no momento de seu agendamento).

### Andamento:
Pretende-se aumentar a complexidade das filas. 
Fazendo ajustes para o modelo de fila simples:
- Separar o escalonador para uma classe exclusiva (Em andamento ainda não vinculado ao projeto)
- Estudar e implementar Priority Queue
- Separar simulação da classe principal

### Objetivos alcançados desde a última entrega:
- Ajustes no gerador de números aleatórios (next) e solução para não gastar memória no simulador com os valores usados.
- Faz a leitura de arquivo.



