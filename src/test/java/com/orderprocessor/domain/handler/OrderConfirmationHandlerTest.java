package com.orderprocessor.domain.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.orderprocessor.domain.model.Order;
import com.orderprocessor.domain.model.OrderItem;
import com.orderprocessor.domain.model.OrderStatus;
import com.orderprocessor.domain.model.Payment;
import com.orderprocessor.domain.model.PaymentMethod;
import com.orderprocessor.domain.model.Product;

public class OrderConfirmationHandlerTest {
    private OrderConfirmationHandler orderConfirmationHandler;

    @Test
    void verificaPedidoAprovado() {
        orderConfirmationHandler = new OrderConfirmationHandler();

        Product produto1 = new Product("Notebook", BigDecimal.valueOf(100), 10);
        OrderItem orderItem = new OrderItem(produto1, 3);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        Order order = new Order("Cliente teste", items, PaymentMethod.PIX);
        order.process();
        Payment payment = new Payment(PaymentMethod.PIX, order.calculaFinalAmount());
        payment.approve();
        order.setPayment(payment);

        boolean resultado = orderConfirmationHandler.processar(order);

        assertTrue(resultado);
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
        assertEquals(7, produto1.getStockQuantity());
    }

    @Test 
    void verificaSeEstoqueNaoFoiReduzidoAposUmItemSemEstoque(){
        orderConfirmationHandler = new OrderConfirmationHandler();

        Product produto1 = new Product("Notebook", BigDecimal.valueOf(100), 10);
        Product produto2 = new Product("Mouse", BigDecimal.valueOf(200), 3);
        OrderItem orderItem = new OrderItem(produto1, 3);
        OrderItem orderItem2 = new OrderItem(produto2, 5);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        items.add(orderItem2);
        Order order = new Order("Cliente teste", items, PaymentMethod.PIX);
        order.process();
        Payment payment = new Payment(PaymentMethod.PIX, order.calculaFinalAmount());
        payment.approve();
        order.setPayment(payment);

        boolean resultado = orderConfirmationHandler.processar(order);

        assertFalse(resultado);
        assertEquals(OrderStatus.REJECTED, order.getStatus());
        assertEquals(10, produto1.getStockQuantity());
        assertEquals(3, produto2.getStockQuantity());
    }

    @Test 
    void verificaConfirmacaoComPagamentoNaoAprovado(){
        orderConfirmationHandler = new OrderConfirmationHandler();

        Product produto1 = new Product("Notebook", BigDecimal.valueOf(100), 10);
        Product produto2 = new Product("Mouse", BigDecimal.valueOf(200), 3);
        OrderItem orderItem = new OrderItem(produto1, 3);
        OrderItem orderItem2 = new OrderItem(produto2, 5);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        items.add(orderItem2);
        Order order = new Order("Cliente teste", items, PaymentMethod.PIX);
        order.process();
        Payment payment = new Payment(PaymentMethod.PIX, order.calculaFinalAmount());
        payment.reject();
        order.setPayment(payment);

        boolean resultado = orderConfirmationHandler.processar(order);

        assertFalse(resultado);
        assertEquals(OrderStatus.REJECTED, order.getStatus());
        assertEquals(10, produto1.getStockQuantity());
        assertEquals(3, produto2.getStockQuantity());
    }

    @Test 
    void verificaPedidoForaEstadoProcessing(){
        orderConfirmationHandler = new OrderConfirmationHandler();

        Product produto1 = new Product("Notebook", BigDecimal.valueOf(100), 10);
        Product produto2 = new Product("Mouse", BigDecimal.valueOf(200), 3);
        OrderItem orderItem = new OrderItem(produto1, 3);
        OrderItem orderItem2 = new OrderItem(produto2, 5);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        items.add(orderItem2);
        Order order = new Order("Cliente teste", items, PaymentMethod.PIX);
        Payment payment = new Payment(PaymentMethod.PIX, order.calculaFinalAmount());
        payment.approve();
        order.setPayment(payment);

        boolean resultado = orderConfirmationHandler.processar(order);

        assertFalse(resultado);
        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertEquals(10, produto1.getStockQuantity());
        assertEquals(3, produto2.getStockQuantity());
    }
}