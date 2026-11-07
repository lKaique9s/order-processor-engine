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

public class DiscountHandlerTest {
    private DiscountHandler discountHandler;

    @Test
    void descontoDe400DeveSer12() {
        discountHandler = new DiscountHandler();

        Product produto1 = new Product("Notebook", BigDecimal.valueOf(400), 10);
        OrderItem orderItem = new OrderItem(produto1, 1);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        Order order = new Order("Cliente teste", items, PaymentMethod.PIX);

        boolean resultado = discountHandler.processar(order);

        assertTrue(resultado);
        assertEquals(order.getDiscountAmount(), BigDecimal.valueOf(12).setScale(2));
    }

    @Test
    void descontoDe600DeveSer60() {
        discountHandler = new DiscountHandler();

        Product produto1 = new Product("Notebook", BigDecimal.valueOf(600), 10);
        OrderItem orderItem = new OrderItem(produto1, 1);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        Order order = new Order("Cliente teste", items, PaymentMethod.PIX);

        boolean resultado = discountHandler.processar(order);

        assertTrue(resultado);
        assertEquals(order.getDiscountAmount(), BigDecimal.valueOf(60).setScale(2));

    }

    @Test
    void descontoDe500DeveSer15() {
        discountHandler = new DiscountHandler();

        Product produto1 = new Product("Notebook", BigDecimal.valueOf(500), 10);
        OrderItem orderItem = new OrderItem(produto1, 1);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        Order order = new Order("Cliente teste", items, PaymentMethod.PIX);

        boolean resultado = discountHandler.processar(order);

        assertTrue(resultado);
        assertEquals(order.getDiscountAmount(), BigDecimal.valueOf(15).setScale(2));
    }

    @Test 
    void descontoMaisDe500(){
        discountHandler = new DiscountHandler();

        Product produto1 = new Product("Notebook", BigDecimal.valueOf(501), 10);
        OrderItem orderItem = new OrderItem(produto1, 1);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        Order order = new Order("Cliente teste", items, PaymentMethod.PIX);

        boolean resultado = discountHandler.processar(order);

        assertTrue(resultado);
        assertEquals(order.getDiscountAmount(), BigDecimal.valueOf(50.1).setScale(2));
    }
}
