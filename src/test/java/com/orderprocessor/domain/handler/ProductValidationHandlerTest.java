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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductValidationHandlerTest {
    private ProductValidationHandler productValidationHandler;

    @Test
    void produtosValidoDeveProsseguir() {
        productValidationHandler = new ProductValidationHandler();

        Product produto1 = new Product("Notebook", BigDecimal.valueOf(1), 10);
        OrderItem orderItem = new OrderItem(produto1, 1);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        Order order = new Order("Cliente teste", items, PaymentMethod.PIX);

        boolean resultado = productValidationHandler.processar(order);

        assertTrue(resultado);
    }

    @Test
    void pedidoNulosDeveSerRejeitado() {
        productValidationHandler = new ProductValidationHandler();
        boolean resultado = productValidationHandler.processar(null);
        assertFalse(resultado);
    }

    @Test 
    void pedidoValidoDeveSerEncaminhadoParaProximoHandler(){
        productValidationHandler = new ProductValidationHandler();

        Product produto1 = new Product("Notebook", BigDecimal.valueOf(1), 10);
        OrderItem orderItem = new OrderItem(produto1, 1);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        Order order = new Order("Cliente teste", items, PaymentMethod.PIX);

        OrderHandler nextHandler = mock(OrderHandler.class);
        when(nextHandler.processar(order)).thenReturn(true);
        productValidationHandler.setNextHandler(nextHandler);
        boolean resultado = productValidationHandler.processar(order);

        verify(nextHandler).processar(order);
        assertTrue(resultado);

    }

}
