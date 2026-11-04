package com.orderprocessor;

import java.math.BigDecimal;
import java.util.List;

import com.orderprocessor.domain.handler.OrderHandler;
import com.orderprocessor.domain.handler.OrderProcessingChain;
import com.orderprocessor.domain.model.Order;
import com.orderprocessor.domain.model.OrderItem;
import com.orderprocessor.domain.model.Product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrderProcessorEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderProcessorEngineApplication.class, args);

        // Criando produtos
        Product product1 = new Product(
                "Notebook",
                new BigDecimal("3000.00"),
                10
        );

        Product product2 = new Product(
                "Mouse",
                new BigDecimal("100.00"),
                5
        );

        // Criando itens do pedido
        OrderItem item1 = new OrderItem(product1, 15);
        OrderItem item2 = new OrderItem(product2, 1);

        // Criando pedido
        Order order = new Order(
                "João",
                List.of(item1, item2)
        );

        // Montando a cadeia
        OrderProcessingChain chain = new OrderProcessingChain();

        // Obtendo o primeiro handler
        OrderHandler firstHandler = chain.getPrimeiroHandler();

        // Processando pedido
        boolean result = firstHandler.processar(order);

        System.out.println("Pedido aprovado? " + result);
    }
}