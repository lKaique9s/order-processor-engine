# Order Processor Engine

API REST para processamento de pedidos, desenvolvida com Java e Spring Boot.

Projeto de estudo focado na aplicação do padrão **Chain of Responsibility**, nos princípios de orientação a objetos e em boas práticas de arquitetura de software.

## Objetivo

Construir uma aplicação capaz de processar pedidos de e-commerce por meio de uma sequência de etapas independentes.

Cada etapa será representada por um handler, responsável por executar uma operação específica e decidir se o pedido pode continuar sendo processado.

O projeto será desenvolvido incrementalmente, priorizando a compreensão dos conceitos, a qualidade do código e a capacidade de tomar decisões de arquitetura.

## Tecnologias

### Base do projeto

- Java 21
- Spring Boot
- Spring Web
- Bean Validation
- Maven

### Tecnologias planejadas

- Spring Data JPA
- PostgreSQL
- JUnit
- Mockito
- Docker
- Swagger / OpenAPI

As tecnologias serão introduzidas conforme a necessidade de cada etapa do desenvolvimento.

## Funcionalidades planejadas

- Cadastro e consulta de produtos.
- Criação e consulta de pedidos.
- Validação dos dados dos pedidos.
- Verificação da existência dos produtos.
- Verificação e controle de estoque.
- Aplicação de descontos.
- Cálculo de frete.
- Simulação de pagamento.
- Confirmação ou rejeição de pedidos.
- Persistência dos dados.
- Testes automatizados.

## Design Pattern

O principal padrão de projeto utilizado será **Chain of Responsibility**.

O processamento de um pedido será organizado em uma cadeia de handlers independentes. Cada handler terá uma responsabilidade específica e poderá:

- Executar sua etapa de processamento.
- Encaminhar o pedido para o próximo handler.
- Interromper o processamento quando uma condição impedir sua continuidade.

A estrutura permitirá adicionar, remover ou reorganizar etapas com menor acoplamento entre as responsabilidades.

## Organização e arquitetura

O projeto seguirá uma organização baseada na separação de responsabilidades, utilizando conceitos de orientação a objetos e princípios SOLID.

A modelagem inicial do domínio contempla:

- `Product`: representa um produto e controla suas operações de estoque.
- `OrderItem`: representa um produto dentro de um pedido, registrando quantidade e preço unitário.
- `Order`: representa o pedido, seus itens, valores e estado atual.
- `OrderStatus`: define os possíveis estados de um pedido.
- `Payment`: representa o pagamento associado ao pedido.
- `PaymentMethod`: define os métodos de pagamento disponíveis.
- `PaymentStatus`: representa o estado do pagamento.

A arquitetura será evoluída gradualmente conforme novas funcionalidades forem implementadas.

## Decisões técnicas

Esta seção documenta algumas decisões tomadas durante o desenvolvimento, explicando os motivos e os problemas que cada escolha busca resolver.

### Modelagem do domínio

- **Responsabilidade do estoque no Product:** as operações de adição e remoção de estoque ficam na própria classe `Product`, pois o produto é responsável por controlar sua quantidade disponível.

- **Registro do preço no OrderItem:** o preço unitário é armazenado no momento da criação do item, em vez de ser consultado continuamente no produto. Dessa forma, alterações futuras no preço de um produto não modificam o valor de pedidos já criados.

- **Encapsulamento das coleções:** a lista de itens do pedido é mantida internamente, e o getter retorna uma cópia não modificável. Isso evita alterações externas diretas na coleção.

- **Uso de BigDecimal:** valores monetários são representados com `BigDecimal` para evitar problemas de precisão comuns em operações financeiras realizadas com tipos de ponto flutuante.

### Processamento dos pedidos

- **Chain of Responsibility:** cada etapa do processamento possui um handler independente. Isso reduz o acoplamento entre as regras de negócio e facilita a manutenção e evolução da cadeia.

- **Separação das responsabilidades:** validação, verificação de estoque, descontos, frete, pagamento e confirmação são responsabilidades de handlers distintos.

- **Ordem de execução dos handlers:** a sequência foi definida para que as validações ocorram antes das operações financeiras e a confirmação aconteça somente após a aprovação do pagamento.

- **Montagem centralizada da cadeia:** a classe `OrderProcessingChain` é responsável por instanciar e conectar os handlers, evitando que essa configuração fique espalhada pelo código.

### Regras de negócio

- **Limite de cheque de estoque antes da baixa:** o estoque é verificado antes do pagamento, mas só é efetivamente reduzido depois da aprovação. Assim, pedidos com pagamento rejeitado não alteram o estoque.

- **Revalidação de estoque na confirmação:** o estoque é verificado novamente antes da baixa. Isso reduz o risco de utilizar informações desatualizadas da verificação anterior.

- **Validação antes da alteração:** a baixa de estoque utiliza dois loops: primeiro verifica se todos os produtos possuem quantidade suficiente; depois realiza as remoções. Isso evita baixas parciais em caso de estoque insuficiente no cenário sequencial.

- **Exceção específica para falta de estoque:** `NoStockQuantityException` representa uma situação de negócio específica, permitindo distinguir a falta de estoque de outras falhas de processamento.

- **Criação do pagamento no PaymentHandler:** o objeto `Payment` só é criado quando o pedido chega à etapa de pagamento, evitando representar um pagamento antes de ele ser efetivamente processado.

- **Simulação de pagamento:** PIX e cartão de débito são aprovados automaticamente. Cartões de crédito são aprovados quando o valor não ultrapassa R$ 5.000,00. Essa regra permite testar diferentes resultados sem integração com um gateway real.

- **Descontos calculados sobre o subtotal original:** pedidos de até R$ 500,00 recebem 3% de desconto; pedidos acima desse valor recebem 10%.

- **Frete calculado sobre o subtotal original:** pedidos de até R$ 100,00 pagam R$ 20,00; pedidos acima de R$ 100,00 e até R$ 500,00 pagam R$ 15,00; acima de R$ 500,00 possuem frete grátis.

### Controle de estado e valores monetários

- **Transições de estado controladas:** em vez de permitir alterações livres por meio de um `setStatus()`, a classe `Order` possui métodos específicos como `process()`, `confirm()` e `reject()`. Cada método controla quais transições são permitidas.

- **Prevenção de processamento duplicado:** um pedido só pode iniciar o processamento quando está no estado `CREATED`. Pedidos em processamento, confirmados ou rejeitados não podem iniciar novamente.

- **Arredondamento centralizado no domínio:** os valores de desconto e frete são arredondados para duas casas decimais nos respectivos setters, enquanto o valor final é arredondado no cálculo do pedido. Isso centraliza a regra de precisão monetária na classe `Order`.

## Progresso

🚧 Em desenvolvimento.

| Etapa | Descrição | Status |
|---|---|---|
| 1 | Fundamentos e definição do domínio | Concluída |
| 2 | Modelagem das classes Java | Concluída |
| 3 | Implementação do Chain of Responsibility | Concluída |
| 4 | Regras de negócio | Concluída |
| 5 | API REST com Spring Boot | Pendente |
| 6 | Persistência com JPA e PostgreSQL | Pendente |
| 7 | Testes e qualidade de código | Pendente |
| 8 | Finalização e preparação para portfólio | Pendente |

## Objetivo de aprendizado

Este projeto faz parte dos estudos de Java e Spring Boot, com foco em:

- Compreender profundamente o padrão Chain of Responsibility.
- Aplicar princípios SOLID e boas práticas de orientação a objetos.
- Desenvolver uma API REST.
- Trabalhar com persistência de dados.
- Escrever testes automatizados.
- Desenvolver autonomia na construção e evolução de projetos de software.