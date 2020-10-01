Desenvolvedoras: Daniela Rigoli e Franciele Constante

## Trabalho 1 - Parte 1

### Descrição:
Nessa primeira versão o projeto contém um gerador de números aleatórios e executa a simulação de uma fila simples recebendo os dados passados pelo usuário.

### Andamento:
Pretende-se implementar para receber as variáveis de um arquivo, aumentar a complexidade das filas. 
Fazendo ajustes para o modelo de fila simples:
- Separar o escalonador para uma classe exclusiva (Em andamento ainda não vinculado ao projeto)
- Estudar e implementar Priority Queue
- Separar simulação da classe principal
- Arrumar gerador de números aleatórios para ter um .next e remover armazamento de main
Além disso, se der tempo pretendemos que apareça no resultado final apenas estados da fila que ocorreram, ou seja, se a fila não passou de 3 pessoas os estados acima de 3 não serão exibidos.

### Objetivos alcançados:
- Resultado igual ao simulado de comparação utilizando os mesmos valores nas operações.
- Conserto do gerador de números aleatórios.

### Classes:
- Evento: Armazena o tipo (se é de chagada ou saída) e o tempo desse evento;
- Fila: Guarda os dados individuais de cada fila e faz o gerenciamento dos métodos relacionados a mesma (adição de pessoa, remoção, estado da fila e gets utilizados);
- GeradorNumerosAleatórios: Atualmente usado para gerar n números aleátorios ou utilizar valores pré definidos para as operações.
- Resultados: Criado para facilitar a geração da média de _n_ simulações, armazena o resultado de cada uma das simulações concluidas.
- Simulador: Atual classe principal. Na main organaniza quantas simulações devem ser feitas e quais os dados utilzados além de mostrar o resultado final. No método filaSimples executa todas as funções necessárias da simulação.

- Escalonador: Irá gerenciar os eventos do simulador (não implementado atualmente)




