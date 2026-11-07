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
import com.orderprocessor.domain.model.PaymentMethod;
import com.orderprocessor.domain.model.PaymentStatus;
import com.orderprocessor.domain.model.Product;

public class PaymentHandlerTest {
    private PaymentHandler paymentHandler;

    @Test
    void pagamentoPixQualquerValorTemQueSerValidoValorBaixo() {
        paymentHandler = new PaymentHandler();

        Product produto1 = new Product("Notebook", BigDecimal.valueOf(1), 10);
        OrderItem orderItem = new OrderItem(produto1, 1);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        Order order = new Order("Cliente teste", items, PaymentMethod.PIX);

        boolean resultado = paymentHandler.processar(order);

        assertEquals(order.calculaFinalAmount(), order.getPayment().getAmount());

        assertTrue(resultado);
        assertEquals(PaymentStatus.APPROVED, order.getPayment().getPaymentStatus());
    }

    @Test
    void pagamentoPixQualquerValorTemQueSerValidoValorAlto() {
        paymentHandler = new PaymentHandler();

        Product produto1 = new Product("Notebook", BigDecimal.valueOf(9999999), 10);
        OrderItem orderItem = new OrderItem(produto1, 1);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        Order order = new Order("Cliente teste", items, PaymentMethod.PIX);

        boolean resultado = paymentHandler.processar(order);

        assertTrue(resultado);
        assertEquals(PaymentStatus.APPROVED, order.getPayment().getPaymentStatus());
    }

    @Test
    void pagamentoDebitCardQualquerValorTemQueSerValidoValorBaixo() {
        paymentHandler = new PaymentHandler();

        Product produto1 = new Product("Notebook", BigDecimal.valueOf(1), 10);
        OrderItem orderItem = new OrderItem(produto1, 1);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        Order order = new Order("Cliente teste", items, PaymentMethod.DEBIT_CARD);

        boolean resultado = paymentHandler.processar(order);

        assertTrue(resultado);
        assertEquals(PaymentStatus.APPROVED, order.getPayment().getPaymentStatus());
    }

    @Test
    void pagamentoDebitCardQualquerValorTemQueSerValidoValorAlto() {
        paymentHandler = new PaymentHandler();

        Product produto1 = new Product("Notebook", BigDecimal.valueOf(999999), 10);
        OrderItem orderItem = new OrderItem(produto1, 1);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        Order order = new Order("Cliente teste", items, PaymentMethod.DEBIT_CARD);

        boolean resultado = paymentHandler.processar(order);

        assertTrue(resultado);
        assertEquals(PaymentStatus.APPROVED, order.getPayment().getPaymentStatus());
    }

    @Test
    void pagamentoCreditCardAte5000TemQueSerAceito() {
        paymentHandler = new PaymentHandler();

        Product produto1 = new Product("Notebook", BigDecimal.valueOf(5000), 10);
        OrderItem orderItem = new OrderItem(produto1, 1);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        Order order = new Order("Cliente teste", items, PaymentMethod.CREDIT_CARD);

        boolean resultado = paymentHandler.processar(order);

        assertTrue(resultado);
        assertEquals(order.calculaFinalAmount(), order.getPayment().getAmount());
        assertEquals(PaymentStatus.APPROVED, order.getPayment().getPaymentStatus());
    }

    @Test
    void pagamentoCreditCardAcimaDe5000TemQueSerRejeitado() {
        paymentHandler = new PaymentHandler();

        Product produto1 = new Product("Notebook", BigDecimal.valueOf(5001), 10);
        OrderItem orderItem = new OrderItem(produto1, 1);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        Order order = new Order("Cliente teste", items, PaymentMethod.CREDIT_CARD);

        order.process();
        boolean resultado = paymentHandler.processar(order);

        assertFalse(resultado);
        assertEquals(PaymentStatus.REJECTED, order.getPayment().getPaymentStatus());
        assertEquals(OrderStatus.REJECTED, order.getStatus());
    }
}
