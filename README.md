# Order Processor Engine

Uma API REST para processamento de pedidos, desenvolvida com Java e
Spring Boot.

Projeto de estudo focado no padrão **Chain of Responsibility**,
arquitetura de software e boas práticas de desenvolvimento.

## Objetivo

Construir uma aplicação que processe pedidos por meio de uma sequência
de etapas independentes, como validação, verificação de estoque, cálculo
de descontos, frete e pagamento simulado.

O projeto será desenvolvido incrementalmente, priorizando a compreensão
do padrão Chain of Responsibility e sua aplicação prática.

## Tecnologias

-   Java 21
-   Spring Boot
-   Spring Web
-   Spring Data JPA
-   Bean Validation
-   PostgreSQL
-   Maven
-   JUnit
-   Mockito

As tecnologias serão adicionadas e utilizadas conforme o desenvolvimento
do projeto.

## Funcionalidades planejadas

-   Cadastro e consulta de produtos
-   Criação e consulta de pedidos
-   Validação dos pedidos
-   Verificação de estoque
-   Aplicação de descontos
-   Cálculo de frete
-   Simulação de pagamento
-   Persistência dos dados
-   Testes automatizados

## Design Pattern

O padrão principal do projeto será **Chain of Responsibility**.

A proposta é organizar o processamento de um pedido em uma cadeia de
handlers, onde cada etapa executa uma responsabilidade específica e
decide se o pedido pode continuar sendo processado.

## Status

🚧 Em desenvolvimento.

## Objetivo de aprendizado

Este projeto faz parte dos estudos de Java e Spring Boot, com foco em
aplicar Design Patterns, desenvolver uma API REST e praticar decisões de
arquitetura de software.
