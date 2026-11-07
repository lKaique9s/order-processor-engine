package com.orderprocessor.domain.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.orderprocessor.domain.model.Order;
import com.orderprocessor.domain.model.OrderItem;
import com.orderprocessor.domain.model.PaymentMethod;
import com.orderprocessor.domain.model.Product;

public class ShippingHandlerTest {
    private ShippingHandler shippingHandler;

    @Test
    void freteDe80DeveSer20() {
        shippingHandler = new ShippingHandler();

        Product produto1 = new Product("Notebook", BigDecimal.valueOf(80), 10);
        OrderItem orderItem = new OrderItem(produto1, 1);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        Order order = new Order("Cliente teste", items, PaymentMethod.PIX);

        boolean resultado = shippingHandler.processar(order);

        assertTrue(resultado);
        assertEquals(BigDecimal.valueOf(20).setScale(2), order.getShippingAmount());
    }

    @Test
    void freteDe100DeveSer20() {
        shippingHandler = new ShippingHandler();

        Product produto1 = new Product("Notebook", BigDecimal.valueOf(100), 10);
        OrderItem orderItem = new OrderItem(produto1, 1);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        Order order = new Order("Cliente teste", items, PaymentMethod.PIX);

        boolean resultado = shippingHandler.processar(order);

        assertTrue(resultado);
        assertEquals(BigDecimal.valueOf(20).setScale(2), order.getShippingAmount());
    }

    @Test
    void freteDe101DeveSer15() {
        shippingHandler = new ShippingHandler();

        Product produto1 = new Product("Notebook", BigDecimal.valueOf(101), 10);
        OrderItem orderItem = new OrderItem(produto1, 1);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        Order order = new Order("Cliente teste", items, PaymentMethod.PIX);

        boolean resultado = shippingHandler.processar(order);

        assertTrue(resultado);
        assertEquals(BigDecimal.valueOf(15).setScale(2), order.getShippingAmount());
    }

    @Test
    void freteNoLimiteDe500DeveSer15() {
        shippingHandler = new ShippingHandler();

        Product produto1 = new Product("Notebook", BigDecimal.valueOf(500), 10);
        OrderItem orderItem = new OrderItem(produto1, 1);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        Order order = new Order("Cliente teste", items, PaymentMethod.PIX);

        boolean resultado = shippingHandler.processar(order);

        assertTrue(resultado);
        assertEquals(BigDecimal.valueOf(15).setScale(2), order.getShippingAmount());
    }

    @Test
    void freteAcimaDe500DeveSerGratis() {
        shippingHandler = new ShippingHandler();

        Product produto1 = new Product("Notebook", BigDecimal.valueOf(501), 10);
        OrderItem orderItem = new OrderItem(produto1, 1);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        Order order = new Order("Cliente teste", items, PaymentMethod.PIX);

        boolean resultado = shippingHandler.processar(order);

        assertTrue(resultado);
        assertEquals(BigDecimal.valueOf(0).setScale(2), order.getShippingAmount());
    }

}
