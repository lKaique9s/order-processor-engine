# Order Processor Engine

API REST para processamento de pedidos de e-commerce, desenvolvida com
**Java 21** e **Spring Boot**.

Projeto de estudo com foco na aplicação do padrão **Chain of
Responsibility**, em orientação a objetos, separação de
responsabilidades, persistência relacional e testes automatizados.

## Objetivo

Construir uma aplicação capaz de processar pedidos por meio de uma
sequência de etapas independentes. Cada etapa é representada por um
*handler*, responsável por executar uma operação específica e decidir se
o pedido pode continuar no fluxo.

O projeto foi desenvolvido incrementalmente, priorizando a compreensão
dos conceitos, a qualidade do código e a tomada de decisões de
arquitetura.

## Tecnologias

-   Java 21
-   Spring Boot 4.1.2
-   Spring Web MVC
-   Spring Data JPA
-   Hibernate
-   PostgreSQL
-   Jakarta Bean Validation
-   Maven
-   JUnit 5
-   Mockito
-   Docker e Docker Compose
-   Swagger / OpenAPI

## Funcionalidades

-   Consulta de produtos por ID.
-   Criação e processamento de pedidos via API REST.
-   Validação dos dados recebidos.
-   Verificação da existência dos produtos.
-   Verificação e controle de estoque.
-   Aplicação de descontos.
-   Cálculo de frete.
-   Simulação de pagamento.
-   Confirmação ou rejeição de pedidos.
-   Persistência de produtos, pedidos, itens e pagamentos.
-   Persistência de pedidos rejeitados por regras de negócio.
-   Tratamento global de exceções.
-   Controle de concorrência otimista no estoque de produtos.
-   Testes unitários e de integração.

> A consulta de pedidos ainda não está disponível. O endpoint
> `GET /api/orders` é utilizado como verificação de funcionamento da
> API.

## Design Pattern: Chain of Responsibility

O principal padrão de projeto utilizado é **Chain of Responsibility**.

O processamento é organizado em uma cadeia de handlers independentes.
Cada handler possui uma responsabilidade específica e pode:

-   Executar sua etapa de processamento.
-   Encaminhar o pedido para o próximo handler.
-   Interromper o processamento quando uma condição impedir sua
    continuidade.

Essa estrutura reduz o acoplamento entre as etapas e facilita a
manutenção e a evolução do fluxo.

### Cadeia de processamento

A sequência atual é:

1.  `OrderValidationHandler`: valida os dados principais do pedido e
    inicia seu processamento.
2.  `ProductValidationHandler`: verifica os dados dos produtos e dos
    itens.
3.  `StockValidationHandler`: verifica a disponibilidade de estoque.
4.  `DiscountHandler`: calcula e aplica o desconto.
5.  `ShippingHandler`: calcula o frete.
6.  `PaymentHandler`: simula o processamento do pagamento.
7.  `OrderConfirmationHandler`: revalida o estoque, realiza a baixa e
    confirma o pedido.

A classe `OrderProcessingChain` conecta os handlers na ordem definida.
Os handlers são componentes gerenciados pelo Spring e recebem suas
dependências por injeção de dependência.

## Organização e arquitetura

O projeto utiliza separação de responsabilidades e conceitos de
orientação a objetos e princípios SOLID.

### Organização das camadas

-   `controller`: recebe requisições HTTP e define as respostas da API.
-   `application.dto`: contém os objetos de transferência de dados das
    requisições e respostas.
-   `application.service`: coordena os casos de uso da aplicação.
-   `domain`: concentra entidades, regras de negócio, handlers e
    exceções do domínio.
-   `infrastructure`: contém componentes de infraestrutura, configuração
    e repositórios de persistência.

### Modelagem do domínio

-   `Product`: representa um produto e controla suas operações de
    estoque.
-   `OrderItem`: representa um produto dentro de um pedido, registrando
    quantidade e preço unitário.
-   `Order`: representa o pedido, seus itens, valores e estado atual.
-   `OrderStatus`: define os estados possíveis de um pedido.
-   `Payment`: representa o pagamento associado ao pedido.
-   `PaymentMethod`: define os métodos de pagamento disponíveis.
-   `PaymentStatus`: representa o estado do pagamento.

## API REST

Endpoints disponibilizados:

  -----------------------------------------------------------------------
  Método                  Endpoint                Descrição
  ----------------------- ----------------------- -----------------------
  GET                     `/api/orders`           Verificação de
                                                  funcionamento da API.
                                                  Não consulta pedidos.

  POST                    `/api/orders`           Recebe e processa um
                                                  pedido.

  GET                     `/api/products/{id}`    Consulta um produto
                                                  pelo ID.
  -----------------------------------------------------------------------

### Exemplo de criação de pedido

**Requisição**

`POST /api/orders`

``` json
{
  "customerName": "João",
  "paymentMethod": "PIX",
  "items": [
    {
      "productId": 1,
      "quantity": 2
    },
    {
      "productId": 2,
      "quantity": 1
    }
  ]
}
```

**Exemplo de resposta**

``` json
{
  "customerName": "João",
  "discountAmount": 610.00,
  "finalAmount": 5490.00,
  "items": [
    {
      "productName": "Notebook",
      "quantity": 2,
      "subtotal": 6000,
      "unitPrice": 3000
    },
    {
      "productName": "Mouse",
      "quantity": 1,
      "subtotal": 100,
      "unitPrice": 100
    }
  ],
  "paymentMethod": "PIX",
  "shippingAmount": 0.00,
  "status": "CONFIRMED",
  "totalAmount": 6100
}
```

Os valores acima são ilustrativos e dependem dos produtos cadastrados e
das regras de negócio aplicadas.

### DTOs

-   `CreateOrderRequest`: define os dados aceitos para criação de um
    pedido.
-   `CreateOrderItemRequest`: define o produto e a quantidade de cada
    item.
-   `OrderResponse`: define as informações retornadas após o
    processamento.
-   `OrderItemResponse`: representa os itens presentes na resposta.

A separação entre DTOs e entidades evita expor diretamente os objetos
internos do domínio e permite controlar quais informações entram e saem
da API.

### Respostas HTTP

  -----------------------------------------------------------------------
  Status                              Descrição
  ----------------------------------- -----------------------------------
  `201 Created`                       Pedido criado e confirmado.

  `400 Bad Request`                   Requisição inválida.

  `404 Not Found`                     Produto não encontrado.

  `409 Conflict`                      Conflito de concorrência otimista.

  `422 Unprocessable Content`         Pedido rejeitado por uma regra de
                                      negócio, como pagamento recusado ou
                                      estoque insuficiente.
  -----------------------------------------------------------------------

## Regras de negócio simuladas

### Pagamento

-   PIX e cartão de débito são aprovados automaticamente.
-   Cartões de crédito são aprovados quando o valor não ultrapassa R\$
    5.000,00.
-   Cartões de crédito acima de R\$ 5.000,00 são recusados.

Não existe integração com um gateway de pagamento real; o comportamento
é simulado para exercitar o fluxo de processamento.

### Descontos

-   Pedidos de até R\$ 500,00 recebem 3% de desconto.
-   Pedidos acima de R\$ 500,00 recebem 10% de desconto.

### Frete

-   Pedidos de até R\$ 100,00 pagam R\$ 20,00.
-   Pedidos acima de R\$ 100,00 e até R\$ 500,00 pagam R\$ 15,00.
-   Pedidos acima de R\$ 500,00 possuem frete grátis.

Os descontos e o frete são calculados sobre o subtotal original do
pedido.

## Decisões técnicas relevantes

### Domínio e valores monetários

-   **Estoque encapsulado em `Product`:** as operações de adição e
    remoção ficam na própria entidade, que controla sua quantidade
    disponível.
-   **Preço registrado em `OrderItem`:** o preço unitário é armazenado
    no momento da criação do item. Alterações futuras no preço do
    produto não modificam pedidos já criados.
-   **Coleções protegidas:** a lista de itens do pedido é mantida
    internamente e exposta por meio de uma cópia não modificável.
-   **`BigDecimal`:** utilizado para representar valores monetários,
    evitando problemas de precisão de tipos de ponto flutuante.
-   **Transições de estado controladas:** `Order` utiliza métodos como
    `process()`, `confirm()` e `reject()` em vez de permitir alterações
    livres de status.

### Processamento e estoque

-   Cada etapa do fluxo possui um handler independente.
-   A cadeia é montada centralizadamente por `OrderProcessingChain`.
-   O estoque é verificado antes das etapas finais, mas sua baixa ocorre
    somente na confirmação.
-   O estoque é revalidado antes da baixa, reduzindo o risco de utilizar
    informações desatualizadas.
-   A confirmação verifica os itens antes de realizar as remoções,
    evitando baixas parciais no cenário sequencial testado.
-   Rejeições normais de negócio são representadas pelo status
    `REJECTED` e podem ser persistidas para manter o histórico do
    processamento.

### API e tratamento de erros

-   A injeção de dependência do Spring fornece os componentes e suas
    dependências.
-   `OrderService` coordena a criação do pedido e o início da cadeia.
-   `@Valid` e Jakarta Bean Validation verificam os dados recebidos pela
    API.
-   `GlobalExceptionHandler`, com `@RestControllerAdvice` e
    `@ExceptionHandler`, centraliza a conversão de exceções em respostas
    HTTP.
-   Rejeições de negócio, como falta de estoque ou pagamento recusado,
    são diferentes de exceções que interrompem o fluxo por dados
    inválidos ou recursos inexistentes.

### Persistência, transações e concorrência

-   PostgreSQL é utilizado como banco de dados relacional.
-   `ProductRepository` e `OrderRepository` estendem `JpaRepository`.
-   As entidades `Product`, `Order`, `OrderItem` e `Payment` são
    mapeadas com JPA.
-   `Order` possui relacionamento `@OneToMany` com `OrderItem` e
    `@OneToOne` com `Payment`.
-   O relacionamento com pagamento é opcional, pois um pedido pode ser
    rejeitado antes da etapa de pagamento.
-   `CascadeType.PERSIST` propaga a persistência do pedido para seus
    itens e pagamento, quando existente.
-   `orphanRemoval = true` permite remover do banco itens que deixam de
    pertencer ao pedido.
-   `@Transactional` em `OrderService.receberPedido()` mantém o
    processamento dentro de uma transação.
-   O Hibernate utiliza **Dirty Checking** para detectar alterações em
    entidades gerenciadas, como a redução do estoque, e sincronizá-las
    com o banco.
-   Exceções que provocam rollback desfazem as alterações da transação.
-   Rejeições normais de negócio não provocam rollback automaticamente,
    permitindo persistir pedidos rejeitados.
-   `@Version` em `Product` habilita o **Optimistic Locking**,
    permitindo detectar conflitos quando operações concorrentes tentam
    atualizar uma versão desatualizada do produto.
-   `ProductCatalogInitializer` cadastra produtos de demonstração quando
    a aplicação é inicializada e o banco ainda não possui produtos.

Durante o desenvolvimento, foi utilizado
`spring.jpa.hibernate.ddl-auto=update` para atualizar a estrutura do
banco conforme os mapeamentos das entidades.

## Testes e qualidade

A suíte automatizada utiliza **JUnit 5** e **Mockito**, com testes
unitários e de integração.

Os testes unitários verificam handlers, regras de negócio, limites de
valores, transições de estado, validações e a orquestração do
`OrderService` com dependências simuladas.

Os testes de integração verificam o comportamento entre componentes
reais da aplicação e, quando aplicável, a persistência no PostgreSQL.

Entre os cenários cobertos estão:

-   Validações de entradas nulas, textos em branco e listas vazias.
-   Encadeamento dos handlers.
-   Limites de desconto, frete e aprovação de pagamento.
-   Estoque suficiente, exato e insuficiente.
-   Rejeição sem baixa indevida de estoque.
-   Persistência de pedidos rejeitados por regras de negócio.
-   Não persistência de pedidos com dados inválidos.
-   Tratamento HTTP de exceções.
-   Conflitos de concorrência otimista.

**Resultado atual da suíte:** 45 testes executados, sem falhas ou erros.

### Executar os testes

No Windows (PowerShell):

``` powershell
.\mvnw.cmd test
```

Em ambientes com shell Unix:

``` bash
./mvnw test
```

## Execução com Docker

O projeto possui configuração com Docker e Docker Compose para executar
a aplicação e o PostgreSQL em containers.

1. Crie o arquivo `.env` a partir do modelo disponibilizado:
   - Linux/macOS: `cp .env.example .env`
   - Windows (PowerShell): `Copy-Item .env.example .env`

2. Com o Docker Desktop em execução, inicie os containers:

``` bash
docker compose up --build
```

Para encerrar os containers sem remover os dados persistidos:

``` bash
docker compose down
```

> O arquivo `.env` contém configurações locais e credenciais, permanecendo ignorado pelo Git. Utilize o `.env.example` como gabarito para configurar suas variáveis locais.

## Documentação da API

A documentação interativa da API é disponibilizada por Swagger /
OpenAPI. Com a aplicação em execução, acesse:

`http://localhost:8080/swagger-ui/index.html`

## Status do projeto

**Projeto concluído para fins de estudo e portfólio.**

  Etapa   Descrição                                  Status
  ------- ------------------------------------------ -----------
  1       Fundamentos e definição do domínio         Concluída
  2       Modelagem das classes Java                 Concluída
  3       Implementação do Chain of Responsibility   Concluída
  4       Regras de negócio                          Concluída
  5       API REST com Spring Boot                   Concluída
  6       Persistência com JPA e PostgreSQL          Concluída
  7       Testes e qualidade de código               Concluída
  8       Finalização e preparação para portfólio    Concluída

## Objetivos de aprendizado

Este projeto foi desenvolvido para praticar:

-   O padrão Chain of Responsibility.
-   Orientação a objetos e princípios SOLID.
-   Desenvolvimento de APIs REST com Spring Boot.
-   Persistência com JPA e PostgreSQL.
-   Transações, Dirty Checking e rollback.
-   Controle de concorrência otimista.
-   Testes unitários e de integração.
-   Organização e evolução incremental de uma aplicação.

------------------------------------------------------------------------

Projeto desenvolvido como parte dos estudos de Java e Spring Boot.
