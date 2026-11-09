package com.orderprocessor.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.orderprocessor.application.dto.CreateOrderItemRequest;
import com.orderprocessor.application.dto.CreateOrderRequest;
import com.orderprocessor.domain.exception.InvalidOrderException;
import com.orderprocessor.domain.model.OrderStatus;
import com.orderprocessor.domain.model.PaymentMethod;
import com.orderprocessor.domain.model.Product;
import com.orderprocessor.infrastructure.repository.OrderRepository;
import com.orderprocessor.infrastructure.repository.ProductRepository;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;
import com.orderprocessor.domain.model.Order;

@SpringBootTest
class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void naoDevePersistirPedidoQuandoDadosSaoInvalidos() {
        Product product = productRepository.saveAndFlush(
                new Product(
                        "Produto Teste Pedido Invalido",
                        new BigDecimal("100.00"),
                        10));

        long quantidadePedidosAntes = orderRepository.count();

        CreateOrderItemRequest item = new CreateOrderItemRequest();
        item.setProductId(product.getId());
        item.setQuantity(1);

        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerName("");
        request.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        request.setItems(List.of(item));
        assertThrows(
                InvalidOrderException.class,
                () -> orderService.receberPedido(request));
        long quantidadePedidosDepois = orderRepository.count();

        assertEquals(
                quantidadePedidosAntes,
                quantidadePedidosDepois);
        productRepository.delete(product);
    }

    @Test
    @Transactional
    void devePersistirPedidoQuandoEstoqueForInsuficiente() {
        Product product = productRepository.saveAndFlush(
                new Product(
                        "Produto Teste Estoque Insuficiente",
                        new BigDecimal("100.00"),
                        2));

        long quantidadePedidosAntes = orderRepository.count();

        String customerName = "Cliente Teste " + UUID.randomUUID();

        CreateOrderItemRequest item = new CreateOrderItemRequest();
        item.setProductId(product.getId());
        item.setQuantity(5);

        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerName(customerName);
        request.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        request.setItems(List.of(item));

        var response = orderService.receberPedido(request);

        assertEquals(OrderStatus.REJECTED, response.getStatus());

        assertEquals(
                quantidadePedidosAntes + 1,
                orderRepository.count());

        Order pedidoSalvo = orderRepository.findAll()
                .stream()
                .filter(p -> p.getCustomerName().equals(customerName))
                .findFirst()
                .orElseThrow();

        assertEquals(OrderStatus.REJECTED, pedidoSalvo.getStatus());
        Product productAfter = productRepository.findById(product.getId())
                .orElseThrow();

        assertEquals(2, productAfter.getStockQuantity());
    }
}