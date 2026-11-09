package com.orderprocessor.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.orderprocessor.application.dto.CreateOrderItemRequest;
import com.orderprocessor.application.dto.CreateOrderRequest;
import com.orderprocessor.application.dto.OrderResponse;
import com.orderprocessor.domain.exception.InvalidOrderException;
import com.orderprocessor.domain.handler.OrderProcessingChain;
import com.orderprocessor.domain.model.Order;
import com.orderprocessor.domain.model.OrderStatus;
import com.orderprocessor.domain.model.PaymentMethod;
import com.orderprocessor.domain.model.Product;
import com.orderprocessor.infrastructure.repository.OrderRepository;
import com.orderprocessor.infrastructure.repository.ProductRepository;

class OrderServiceTest {

    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private OrderProcessingChain orderProcessingChain;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        orderRepository = mock(OrderRepository.class);
        orderProcessingChain = mock(OrderProcessingChain.class);

        orderService = new OrderService(
                productRepository,
                orderRepository,
                orderProcessingChain);
    }

    @Test
    void deveLancarExcecaoQuandoPedidoPossuiDadosInvalidos() {

        // Arrange
        CreateOrderRequest request = new CreateOrderRequest();

        // Act e Assert
        assertThrows(
                InvalidOrderException.class,
                () -> orderService.receberPedido(request));

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExiste() {

        CreateOrderRequest request = criarRequestValido();
        when(productRepository.findById(1L))
                .thenReturn(Optional.empty());
        assertThrows(
                RuntimeException.class,
                () -> orderService.receberPedido(request));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void devePersistirPedidoQuandoProcessamentoRetornaFalse() {

        Product product = new Product(
                "Notebook",
                new BigDecimal("1000.00"),
                10);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(orderProcessingChain.getPrimeiroHandler())
                .thenReturn(order -> {
                    order.process();
                    order.reject();
                    return false;
                });
        OrderResponse response = orderService.receberPedido(criarRequestValido());
        assertEquals(OrderStatus.REJECTED, response.getStatus());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void devePersistirPedidoQuandoProcessamentoTemSucesso() {

        Product product = new Product(
                "Notebook",
                new BigDecimal("1000.00"),
                10);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(orderProcessingChain.getPrimeiroHandler())
                .thenReturn(order -> {
                    order.process();
                    order.confirm();
                    return true;
                });
        OrderResponse response = orderService.receberPedido(criarRequestValido());
        assertEquals(OrderStatus.CONFIRMED, response.getStatus());
        verify(orderRepository).save(any(Order.class));
    }

    private CreateOrderRequest criarRequestValido() {

        CreateOrderItemRequest item = new CreateOrderItemRequest();
        item.setProductId(1L);
        item.setQuantity(1);

        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerName("Cliente Teste");
        request.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        request.setItems(List.of(item));

        return request;
    }
}