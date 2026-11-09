package com.orderprocessor.domain.handler;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.orderprocessor.domain.exception.InvalidOrderException;

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
        assertThrows(
                InvalidOrderException.class,
                () -> orderValidationHandler.processar(null));
    }

    @Test
    void pedidoSemCustomerNameDeveSerRejeitado() {
        orderValidationHandler = new OrderValidationHandler();

        Product produto1 = new Product("Notebook", BigDecimal.valueOf(1), 10);
        OrderItem orderItem = new OrderItem(produto1, 1);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        Order order = new Order("", items, PaymentMethod.PIX);

        assertThrows(
                InvalidOrderException.class,
                () -> orderValidationHandler.processar(order));
    }

    @Test
    void pedidoComCustomerNameApenasEspacosDeveSerRejeitado() {
        orderValidationHandler = new OrderValidationHandler();

        Product produto1 = new Product("Notebook", BigDecimal.valueOf(1), 10);
        OrderItem orderItem = new OrderItem(produto1, 1);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        Order order = new Order("        ", items, PaymentMethod.PIX);

        assertThrows(
                InvalidOrderException.class,
                () -> orderValidationHandler.processar(order));
    }

    @Test
    void pedidoSemItensDeveSerRejeitado() {
        orderValidationHandler = new OrderValidationHandler();

        List<OrderItem> items = new ArrayList<>();
        Order order = new Order("Cliente Teste", items, PaymentMethod.PIX);

        assertThrows(
                InvalidOrderException.class,
                () -> orderValidationHandler.processar(order));
    }
}
