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
- `Order`: representa o pedido, seus itens, valor total e estado atual.
- `OrderStatus`: define os possíveis estados de um pedido.

A arquitetura será evoluída gradualmente conforme novas funcionalidades forem implementadas.

## Progresso

🚧 Em desenvolvimento.

| Etapa | Descrição | Status |
|---|---|---|
| 1 | Fundamentos e definição do domínio | Concluída |
| 2 | Modelagem das classes Java | Concluída |
| 3 | Implementação do Chain of Responsibility | Pendente |
| 4 | Regras de negócio | Pendente |
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