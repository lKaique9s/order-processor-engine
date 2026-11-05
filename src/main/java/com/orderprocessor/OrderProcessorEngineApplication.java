
package com.orderprocessor;

import java.math.BigDecimal;
import java.util.List;

import com.orderprocessor.domain.handler.OrderProcessingChain;
import com.orderprocessor.domain.model.Order;
import com.orderprocessor.domain.model.OrderItem;
import com.orderprocessor.domain.model.PaymentMethod;
import com.orderprocessor.domain.model.Product;

import com.orderprocessor.domain.exception.NoStockQuantityException;

public class OrderProcessorEngineApplication {

    public static void main(String[] args) {

        testarPedidoAprovado();
        testarPagamentoRejeitado();
        testarEstoqueInsuficiente();

    }

    private static void testarPedidoAprovado() {
        System.out.println("\n========== TESTE 1: PEDIDO APROVADO ==========");

        Product product = new Product(
            "Notebook", new BigDecimal("1000.00"), 10
        );

        OrderItem item = new OrderItem(product, 2);

        Order order = new Order(
            "Cliente Teste",
            List.of(item),
            PaymentMethod.PIX
        );

        executarPedido(order);

        System.out.println("Status: " + order.getStatus());
        System.out.println("Pagamento: " + order.getPayment().getPaymentStatus());
        System.out.println("Estoque esperado: 8");
        System.out.println("Estoque atual: " + product.getStockQuantity());
    }

    private static void testarPagamentoRejeitado() {
        System.out.println("\n========== TESTE 2: PAGAMENTO REJEITADO ==========");

        Product product = new Product(
            "Computador", new BigDecimal("6000.00"), 10
        );

        OrderItem item = new OrderItem(product, 1);

        Order order = new Order(
            "Cliente Teste",
            List.of(item),
            PaymentMethod.CREDIT_CARD
        );

        executarPedido(order);

        System.out.println("Status: " + order.getStatus());
        System.out.println("Pagamento: " + order.getPayment().getPaymentStatus());
        System.out.println("Estoque esperado: 10");
        System.out.println("Estoque atual: " + product.getStockQuantity());
    }

    private static void testarEstoqueInsuficiente() {
        System.out.println("\n========== TESTE 3: ESTOQUE INSUFICIENTE ==========");

        Product productA = new Product(
            "Mouse", new BigDecimal("100.00"), 10
        );

        Product productB = new Product(
            "Teclado", new BigDecimal("200.00"), 1
        );

        OrderItem itemA = new OrderItem(productA, 2);
        OrderItem itemB = new OrderItem(productB, 3);

        Order order = new Order(
            "Cliente Teste",
            List.of(itemA, itemB),
            PaymentMethod.PIX
        );

        executarPedido(order);

        System.out.println("Status: " + order.getStatus());
        System.out.println("Estoque esperado A: 10");
        System.out.println("Estoque atual A: " + productA.getStockQuantity());
        System.out.println("Estoque esperado B: 1");
        System.out.println("Estoque atual B: " + productB.getStockQuantity());
    }

    private static void executarPedido(Order order) {
        OrderProcessingChain chain = new OrderProcessingChain();

        try {
            boolean resultado = chain.getPrimeiroHandler().processar(order);

            System.out.println("Processamento concluído: " + resultado);
            System.out.println("Subtotal: " + order.getTotalAmount());
            System.out.println("Total final: " + order.calculaFinalAmount());

        } catch (NoStockQuantityException e) {
            System.out.println("Erro de estoque: " + e.getMessage());
        }
    }
}
