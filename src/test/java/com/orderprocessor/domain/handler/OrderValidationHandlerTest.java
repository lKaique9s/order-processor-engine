package com.orderprocessor.domain.handler;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.orderprocessor.domain.model.Order;
import com.orderprocessor.domain.model.OrderItem;
import com.orderprocessor.domain.model.PaymentMethod;
import com.orderprocessor.domain.model.Product;

public class OrderValidationHandlerTest {
    private OrderValidationHandler orderValidationHandler;

    @Test
    void pedidoValidoDeveProsseguir() {
        orderValidationHandler = new OrderValidationHandler();

        Product produto1 = new Product("Notebook", BigDecimal.valueOf(1), 10);
        OrderItem orderItem = new OrderItem(produto1, 1);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        Order order = new Order("Cliente teste", items, PaymentMethod.PIX);

        boolean resultado = orderValidationHandler.processar(order);

        assertTrue(resultado);
    }

    @Test
    void pedidoNuloDeveSerRejeitado() {
        orderValidationHandler = new OrderValidationHandler();
        boolean resultado = orderValidationHandler.processar(null);
        assertFalse(resultado);
    }

    @Test
    void pedidoSemCustomerNameDeveSerRejeitado() {
        orderValidationHandler = new OrderValidationHandler();

        Product produto1 = new Product("Notebook", BigDecimal.valueOf(1), 10);
        OrderItem orderItem = new OrderItem(produto1, 1);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        Order order = new Order("", items, PaymentMethod.PIX);

        boolean resultado = orderValidationHandler.processar(order);
        assertFalse(resultado);
    }

    @Test
    void pedidoComCustomerNameApenasEspacosDeveSerRejeitado() {
        orderValidationHandler = new OrderValidationHandler();

        Product produto1 = new Product("Notebook", BigDecimal.valueOf(1), 10);
        OrderItem orderItem = new OrderItem(produto1, 1);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        Order order = new Order("        ", items, PaymentMethod.PIX);

        boolean resultado = orderValidationHandler.processar(order);
        assertFalse(resultado);
    }

    @Test
    void pedidoSemItensDeveSerRejeitado() {
        orderValidationHandler = new OrderValidationHandler();

        List<OrderItem> items = new ArrayList<>();
        Order order = new Order("Cliente Teste", items, PaymentMethod.PIX);

        boolean resultado = orderValidationHandler.processar(order);
        assertFalse(resultado);
    }
}
