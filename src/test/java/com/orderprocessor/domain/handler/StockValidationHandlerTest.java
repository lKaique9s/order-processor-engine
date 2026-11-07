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
import com.orderprocessor.domain.model.Product;

public class StockValidationHandlerTest {
    private StockValidationHandler stockValidationHandler;

    @Test
    void devePermitirPedidoQuandoExisteEstoqueSuficiente() {
        stockValidationHandler = new StockValidationHandler();

        Product produto1 = new Product("Notebook", BigDecimal.valueOf(3000), 10);
        OrderItem orderItem = new OrderItem(produto1, 5);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        Order order = new Order("Cliente teste", items, PaymentMethod.PIX);

        boolean resultado = stockValidationHandler.processar(order);

        assertTrue(resultado);
    }

    @Test
    void deveRejeitarPedidoQuandoEstoqueForInsuficiente() {
        stockValidationHandler = new StockValidationHandler();

        Product produto1 = new Product("Notebook", BigDecimal.valueOf(3000), 10);
        OrderItem orderItem = new OrderItem(produto1, 15);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        Order order = new Order("Cliente teste", items, PaymentMethod.PIX);

        order.process();
        boolean resultado = stockValidationHandler.processar(order);

        assertFalse(resultado);
        assertEquals(OrderStatus.REJECTED, order.getStatus());
    }

    @Test
    void devePermitirQuandoQuantidadeSolicitadaForIgualAoEstoque() {
        stockValidationHandler = new StockValidationHandler();

        Product produto1 = new Product("Notebook", BigDecimal.valueOf(3000), 10);
        OrderItem orderItem = new OrderItem(produto1, 10);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        Order order = new Order("Cliente teste", items, PaymentMethod.PIX);

        boolean resultado = stockValidationHandler.processar(order);

        assertTrue(resultado);
    }

    @Test
    void devePermitirPedidoQuandoTodosOsItensPossuemEstoqueSuficiente() {
        stockValidationHandler = new StockValidationHandler();

        Product produto1 = new Product("Notebook", BigDecimal.valueOf(3000), 10);
        Product produto2 = new Product("laptop", BigDecimal.valueOf(300), 5);
        OrderItem orderItem = new OrderItem(produto1, 8);
        OrderItem orderItem2 = new OrderItem(produto2, 3);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        items.add(orderItem2);
        Order order = new Order("Cliente teste", items, PaymentMethod.PIX);

        boolean resultado = stockValidationHandler.processar(order);

        assertTrue(resultado);
    }

    @Test
    void deveRejeitarPedidoQuandoUmItemPossuiEstoqueInsuficiente() {
        stockValidationHandler = new StockValidationHandler();

        Product produto1 = new Product("Notebook", BigDecimal.valueOf(3000), 10);
        Product produto2 = new Product("laptop", BigDecimal.valueOf(300), 5);
        OrderItem orderItem = new OrderItem(produto1, 8);
        OrderItem orderItem2 = new OrderItem(produto2, 6);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        items.add(orderItem2);
        Order order = new Order("Cliente teste", items, PaymentMethod.PIX);

        order.process();
        boolean resultado = stockValidationHandler.processar(order);

        assertFalse(resultado);
        assertEquals(OrderStatus.REJECTED, order.getStatus());
    }

    @Test
    void deveRejeitarPedidoQuandoTodosPossuemEstoqueInsuficiente() {
        stockValidationHandler = new StockValidationHandler();

        Product produto1 = new Product("Notebook", BigDecimal.valueOf(3000), 10);
        Product produto2 = new Product("laptop", BigDecimal.valueOf(300), 5);
        OrderItem orderItem = new OrderItem(produto1, 11);
        OrderItem orderItem2 = new OrderItem(produto2, 6);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        items.add(orderItem2);
        Order order = new Order("Cliente teste", items, PaymentMethod.PIX);

        order.process();
        boolean resultado = stockValidationHandler.processar(order);

        assertFalse(resultado);
        assertEquals(OrderStatus.REJECTED, order.getStatus());
    }

    @Test
    void devePermitirPedidoQuandoTodosPossuemEstoqueIgualAoEstoque() {
        stockValidationHandler = new StockValidationHandler();

        Product produto1 = new Product("Notebook", BigDecimal.valueOf(3000), 10);
        Product produto2 = new Product("laptop", BigDecimal.valueOf(300), 5);
        Product produto3 = new Product("Mouse", BigDecimal.valueOf(10), 3);
        OrderItem orderItem = new OrderItem(produto1, 10);
        OrderItem orderItem2 = new OrderItem(produto2, 5);
        OrderItem orderItem3 = new OrderItem(produto3, 3);
        List<OrderItem> items = new ArrayList<>();
        items.add(orderItem);
        items.add(orderItem2);
        items.add(orderItem3);
        Order order = new Order("Cliente teste", items, PaymentMethod.PIX);

        boolean resultado = stockValidationHandler.processar(order);

        assertTrue(resultado);
    }

    @Test
    void deveRejeitarQuandoPedidoForNulo() {
        stockValidationHandler = new StockValidationHandler();
        boolean resultado = stockValidationHandler.processar(null);

        assertFalse(resultado);
    }


}
