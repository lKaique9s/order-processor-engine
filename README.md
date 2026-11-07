# Order Processor Engine

API REST para processamento de pedidos, desenvolvida com Java e Spring Boot.

Projeto de estudo focado na aplicação do padrão **Chain of Responsibility**, nos princípios de orientação a objetos e em boas práticas de arquitetura de software.

## Objetivo

Construir uma aplicação capaz de processar pedidos de e-commerce por meio de uma sequência de etapas independentes.

Cada etapa será representada por um handler, responsável por executar uma operação específica e decidir se o pedido pode continuar sendo processado.

O projeto é desenvolvido incrementalmente, priorizando a compreensão dos conceitos, a qualidade do código e a capacidade de tomar decisões de arquitetura.

## Tecnologias

### Implementadas

- Java 21
- Spring Boot 4.1.2
- Spring Web MVC
- Spring Data JPA
- Hibernate
- PostgreSQL
- Jakarta Bean Validation
- Maven
- JUnit 5
- Mockito

### Planejadas

- JUnit
- Mockito
- Docker
- Swagger / OpenAPI

## Funcionalidades

- [x] Consulta de produtos.
- [x] Criação e processamento de pedidos via API REST.
- [x] Validação dos dados dos pedidos.
- [x] Verificação da existência dos produtos.
- [x] Verificação e controle de estoque.
- [x] Aplicação de descontos.
- [x] Cálculo de frete.
- [x] Simulação de pagamento.
- [x] Confirmação ou rejeição de pedidos.
- [x] Persistência de produtos, pedidos, itens e pagamentos.
- [x] Persistência de pedidos rejeitados.
- [ ] Consulta de pedidos.
- [x] Testes automatizados (testes unitários com JUnit 5 e Mockito).

## Design Pattern

O principal padrão de projeto utilizado é **Chain of Responsibility**.

O processamento de um pedido é organizado em uma cadeia de handlers independentes. Cada handler possui uma responsabilidade específica e pode:

- Executar sua etapa de processamento.
- Encaminhar o pedido para o próximo handler.
- Interromper o processamento quando uma condição impedir sua continuidade.

A estrutura permite adicionar, remover ou reorganizar etapas com menor acoplamento entre as responsabilidades.

### Cadeia de processamento

A sequência atual de handlers é:

1. `OrderValidationHandler`: valida os dados principais do pedido e inicia seu processamento.
2. `ProductValidationHandler`: verifica os dados dos produtos e dos itens.
3. `StockValidationHandler`: verifica a disponibilidade de estoque.
4. `DiscountHandler`: calcula e aplica o desconto.
5. `ShippingHandler`: calcula o frete.
6. `PaymentHandler`: simula o processamento do pagamento.
7. `OrderConfirmationHandler`: revalida o estoque, realiza a baixa e confirma o pedido.

A cadeia é montada pela classe `OrderProcessingChain`, que conecta os handlers na ordem definida.

## Organização e arquitetura

O projeto segue uma organização baseada na separação de responsabilidades, utilizando conceitos de orientação a objetos e princípios SOLID.

### Modelagem do domínio

A modelagem contempla:

- `Product`: representa um produto e controla suas operações de estoque.
- `OrderItem`: representa um produto dentro de um pedido, registrando quantidade e preço unitário.
- `Order`: representa o pedido, seus itens, valores e estado atual.
- `OrderStatus`: define os possíveis estados de um pedido.
- `Payment`: representa o pagamento associado ao pedido.
- `PaymentMethod`: define os métodos de pagamento disponíveis.
- `PaymentStatus`: representa o estado do pagamento.

### Organização das camadas

A aplicação utiliza as seguintes camadas:

- `controller`: recebe requisições HTTP e define as respostas da API.
- `application.dto`: contém os objetos de transferência de dados utilizados nas requisições e respostas.
- `application.service`: coordena os casos de uso da aplicação.
- `domain`: concentra as entidades, regras de negócio, handlers e exceções do domínio.
- `infrastructure`: contém componentes de infraestrutura, configuração e repositórios de persistência.

## API REST

A aplicação disponibiliza os seguintes endpoints:

| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/orders` | Verifica se a API está funcionando. |
| POST | `/api/orders` | Recebe e processa um pedido. |
| GET | `/api/products/{id}` | Consulta um produto pelo ID. |

### Exemplo de criação de pedido

**Requisição**

`POST /api/orders`

```json
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

```json
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

### DTOs

Foram implementados os seguintes DTOs:

- `CreateOrderRequest`: define os dados aceitos para criação de um pedido.
- `CreateOrderItemRequest`: define o produto e a quantidade de cada item.
- `OrderResponse`: define as informações retornadas após o processamento.
- `OrderItemResponse`: representa os itens presentes na resposta.

A separação entre DTOs e entidades evita expor diretamente os objetos internos do domínio e permite controlar quais informações entram e saem da API.

### Respostas HTTP

| Status | Descrição | Exemplo |
|---|---|---|
| 201 Created | Pedido criado e confirmado. | Processamento concluído com sucesso. |
| 400 Bad Request | Requisição inválida. | Falha na validação dos dados de entrada. |
| 404 Not Found | Recurso não encontrado. | Produto inexistente no catálogo. |
| 422 Unprocessable Content | Pedido rejeitado por uma regra de negócio. | Pagamento recusado ou estoque insuficiente. |

## Decisões técnicas

Esta seção documenta algumas decisões tomadas durante o desenvolvimento, explicando os motivos e os problemas que cada escolha busca resolver.

### Modelagem do domínio

- **Responsabilidade do estoque no Product:** as operações de adição e remoção de estoque ficam na própria classe `Product`, pois o produto é responsável por controlar sua quantidade disponível.

- **Registro do preço no OrderItem:** o preço unitário é armazenado no momento da criação do item. Dessa forma, alterações futuras no preço de um produto não modificam o valor de pedidos já criados.

- **Encapsulamento das coleções:** a lista de itens do pedido é mantida internamente, e o getter retorna uma cópia não modificável. Isso evita alterações externas diretas na coleção.

- **Uso de BigDecimal:** valores monetários são representados com `BigDecimal` para evitar problemas de precisão comuns em operações financeiras realizadas com tipos de ponto flutuante.

### Processamento dos pedidos

- **Chain of Responsibility:** cada etapa do processamento possui um handler independente. Isso reduz o acoplamento entre as regras de negócio e facilita a manutenção e evolução da cadeia.

- **Separação das responsabilidades:** validação, verificação de estoque, descontos, frete, pagamento e confirmação são responsabilidades de handlers distintos.

- **Ordem de execução dos handlers:** a sequência foi definida para que as validações ocorram antes das operações financeiras e a confirmação aconteça somente após a aprovação do pagamento.

- **Montagem centralizada da cadeia:** a classe `OrderProcessingChain` é responsável por instanciar e conectar os handlers, evitando que essa configuração fique espalhada pelo código.

### Regras de negócio

- **Verificação de estoque antes da baixa:** o estoque é verificado antes do pagamento, mas só é efetivamente reduzido depois da aprovação. Assim, pedidos com pagamento rejeitado não alteram o estoque.

- **Revalidação de estoque na confirmação:** o estoque é verificado novamente antes da baixa. Isso reduz o risco de utilizar informações desatualizadas da verificação anterior.

- **Validação antes da alteração:** a baixa de estoque utiliza dois loops: primeiro verifica se todos os produtos possuem quantidade suficiente; depois realiza as remoções. Isso evita baixas parciais em caso de estoque insuficiente no cenário sequencial.

- **Exceção específica para falta de estoque:** `NoStockQuantityException` representa uma situação específica de falta de estoque, permitindo distingui-la de outras falhas de processamento.

- **Criação do pagamento no PaymentHandler:** o objeto `Payment` só é criado quando o pedido chega à etapa de pagamento, evitando representar um pagamento antes de ele ser efetivamente processado.

- **Simulação de pagamento:** PIX e cartão de débito são aprovados automaticamente. Cartões de crédito são aprovados quando o valor não ultrapassa R$ 5.000,00. Essa regra permite testar diferentes resultados sem integração com um gateway real.

- **Descontos calculados sobre o subtotal original:** pedidos de até R$ 500,00 recebem 3% de desconto; pedidos acima desse valor recebem 10%.

- **Frete calculado sobre o subtotal original:** pedidos de até R$ 100,00 pagam R$ 20,00; pedidos acima de R$ 100,00 e até R$ 500,00 pagam R$ 15,00; acima de R$ 500,00 possuem frete grátis.

### Controle de estado e valores monetários

- **Transições de estado controladas:** em vez de permitir alterações livres por meio de um `setStatus()`, a classe `Order` possui métodos específicos como `process()`, `confirm()` e `reject()`. Cada método controla quais transições são permitidas.

- **Prevenção de processamento duplicado:** um pedido só pode iniciar o processamento quando está no estado `CREATED`. Pedidos em processamento, confirmados ou rejeitados não podem iniciar novamente.

- **Arredondamento centralizado no domínio:** os valores de desconto e frete são arredondados para duas casas decimais nos respectivos setters, enquanto o valor final é arredondado no cálculo do pedido. Isso centraliza a regra de precisão monetária na classe `Order`.

### API REST e Spring Boot

- **Injeção de dependência:** os componentes gerenciados pelo Spring são fornecidos por injeção de dependência, evitando que as dependências da aplicação sejam instanciadas manualmente em diferentes classes.

- **Separação entre DTOs e entidades:** `CreateOrderRequest` define os dados aceitos pela API, enquanto `OrderResponse` define os dados retornados. Isso evita expor diretamente a entidade `Order` e permite controlar a comunicação com os clientes.

- **Validação na entrada da API:** `@Valid` e as anotações do Jakarta Bean Validation verificam os dados recebidos antes da execução do serviço.

- **Service como coordenador do processamento:** `OrderService` é responsável por transformar os dados da requisição em objetos do domínio e iniciar a cadeia de handlers.

- **Tratamento global de exceções:** `GlobalExceptionHandler`, utilizando `@RestControllerAdvice` e `@ExceptionHandler`, centraliza a conversão de exceções em respostas HTTP.

- **Exceção específica para produto inexistente:** `ProductNotFoundException` representa a tentativa de consultar um produto que não existe no banco de dados, resultando em HTTP 404.

- **Separação entre rejeições de negócio e exceções:** rejeições normais do processamento, como pagamento recusado ou estoque insuficiente identificado durante a validação, são representadas pelo status `REJECTED` e retornadas com HTTP 422. Exceções são utilizadas para situações excepcionais que interrompem o fluxo normal.

- **Status HTTP conforme o resultado:** pedidos confirmados retornam HTTP 201; pedidos rejeitados por regras de negócio retornam HTTP 422; recursos inexistentes retornam HTTP 404.

### Persistência com JPA e PostgreSQL

- **Persistência relacional:** PostgreSQL foi introduzido como banco de dados da aplicação, substituindo o catálogo temporário em memória.

- **Spring Data JPA:** os repositórios `ProductRepository` e `OrderRepository` estendem `JpaRepository`, disponibilizando operações de persistência e consulta sem necessidade de implementar manualmente as operações básicas.

- **Mapeamento das entidades:** `Product`, `Order`, `OrderItem` e `Payment` foram configuradas como entidades JPA utilizando `@Entity`, `@Table`, `@Id` e `@GeneratedValue`.

- **Relacionamento entre pedido e itens:** `Order` utiliza `@OneToMany` e `OrderItem` utiliza `@ManyToOne`. Um pedido pode possuir vários itens, enquanto cada item pertence a um único pedido.

- **Relacionamento entre pedido e pagamento:** foi utilizado `@OneToOne` entre `Order` e `Payment`. A chave estrangeira `payment_id` fica na tabela `orders`.

- **Pagamento opcional:** a associação com `Payment` permite valor nulo, pois um pedido pode ser rejeitado por falta de estoque antes de chegar à etapa de pagamento.

- **Cascade Persist:** a persistência do pedido propaga a persistência dos seus itens e do pagamento associado, quando existente. O relacionamento com `Product` não propaga a persistência, pois produtos são entidades independentes e compartilhadas.

- **Orphan Removal:** `orphanRemoval = true` no relacionamento entre `Order` e `OrderItem` permite remover do banco os itens que deixam de pertencer ao pedido.

- **Transações com @Transactional:** o método `receberPedido()` do `OrderService` utiliza `@Transactional` para manter o processamento do pedido dentro de uma única transação, abrangendo consultas, regras de negócio, atualização do estoque e persistência.

- **Dirty Checking:** alterações realizadas em entidades gerenciadas pelo contexto de persistência, como a redução do estoque de um produto, são detectadas pelo Hibernate e sincronizadas com o banco durante a transação, sem necessidade de executar `save()` explicitamente para cada alteração.

- **Rollback:** exceções que provocam rollback dentro da transação desfazem as alterações realizadas no banco durante aquela operação, preservando a consistência dos dados.

- **Persistência de pedidos rejeitados:** rejeições normais de negócio não provocam rollback automaticamente. Dessa forma, pedidos rejeitados podem ser persistidos para manter o histórico do processamento.

- **Inicialização de produtos de demonstração:** `ProductCatalogInitializer` implementa `CommandLineRunner` para cadastrar produtos automaticamente no PostgreSQL quando o banco ainda não possui produtos.

- **Configuração do Hibernate:** `spring.jpa.hibernate.ddl-auto=update` foi utilizada durante o desenvolvimento para atualizar a estrutura do banco conforme os mapeamentos das entidades.

### Testes e qualidade de código

- **Estratégia de testes unitários isolados:** os handlers da cadeia (`OrderValidationHandler`, `ProductValidationHandler`, `StockValidationHandler`, `DiscountHandler`, `ShippingHandler`, `PaymentHandler` e `OrderConfirmationHandler`) são testados de forma unitária, sem inicializar o contexto do Spring ou banco de dados relacional. Isso garante testes rápidos, determinísticos e de fácil manutenção.

- **Uso de Mockito na camada de serviço:** em `OrderServiceTest`, as dependências (`ProductRepository`, `OrderRepository` e `OrderProcessingChain`) são simuladas via `mock()`, permitindo validar de forma isolada a orquestração do caso de uso, como a criação dos itens do pedido, o lançamento de `ProductNotFoundException`, o salvamento no repositório e a execução da cadeia mesmo quando o pedido é rejeitado.

- **Validação do encadeamento da Chain:** foi utilizado mock do próximo handler (`setNextHandler`) para garantir que os handlers repassam a execução adiante quando as condições de processamento são satisfeitas.

- **Testes de limites (Boundary Testing):** regras de negócio que envolvem faixas de valores foram amplamente cobertas em seus limites exatos:
  - **Frete:** faixas de até R$ 100,00 (R$ 20,00), de R$ 100,01 a R$ 500,00 (R$ 15,00) e acima de R$ 500,00 (frete grátis).
  - **Descontos:** pedidos de até R$ 500,00 (3%) e acima de R$ 500,00 (10%).
  - **Pagamentos com cartão de crédito:** limite de R$ 5.000,00 (aprovado) e acima de R$ 5.000,00 (rejeitado).

- **Revalidação e consistência de estoque na confirmação:** testes unitários cobrem cenários com estoque suficiente, estoque exato e estoque insuficiente. Além disso, foi validada a atomicidade lógica em `OrderConfirmationHandler`: caso um dos itens da lista não possua estoque, a operação é rejeitada e nenhum dos itens anteriores tem seu estoque debitado.

- **Ajuste de tratamento na confirmação do pedido:** na etapa de confirmação, pedidos com pagamento não aprovado ou estoque insuficiente transicionam para o estado `REJECTED` via `order.reject()` e retornam `false`, garantindo que o status seja registrado e persistido de maneira consistente em vez de abortar o fluxo abruptamente.

- **Validações defensivas:** testes cobrem casos de entradas nulas (`null`), campos de texto em branco ou contendo apenas espaços, e coleções de itens vazias.

## Execução dos testes

Para executar a suíte completa de testes automatizados com o Maven Wrapper:

```bash
./mvnw test
```

No Windows (PowerShell / Prompt de Comando):

```powershell
.\mvnw.cmd test
```

## Progresso

🚧 Em desenvolvimento.

| Etapa | Descrição | Status |
|---|---|---|
| 1 | Fundamentos e definição do domínio | Concluída |
| 2 | Modelagem das classes Java | Concluída |
| 3 | Implementação do Chain of Responsibility | Concluída |
| 4 | Regras de negócio | Concluída |
| 5 | API REST com Spring Boot | Concluída |
| 6 | Persistência com JPA e PostgreSQL | Concluída |
| 7 | Testes e qualidade de código | Concluída |
| 8 | Finalização e preparação para portfólio | Pendente |

## Objetivo de aprendizado

Este projeto faz parte dos estudos de Java e Spring Boot, com foco em:

- Compreender profundamente o padrão Chain of Responsibility.
- Aplicar princípios SOLID e boas práticas de orientação a objetos.
- Desenvolver uma API REST.
- Trabalhar com persistência de dados utilizando JPA e PostgreSQL.
- Compreender transações, Dirty Checking e rollback.
- Escrever testes automatizados.
- Desenvolver autonomia na construção e evolução de projetos de software.