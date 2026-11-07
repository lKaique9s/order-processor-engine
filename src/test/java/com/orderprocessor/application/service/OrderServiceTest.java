package com.orderprocessor.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.orderprocessor.application.dto.CreateOrderItemRequest;
import com.orderprocessor.application.dto.CreateOrderRequest;
import com.orderprocessor.domain.exception.ProductNotFoundException;
import com.orderprocessor.domain.handler.OrderHandler;
import com.orderprocessor.domain.handler.OrderProcessingChain;
import com.orderprocessor.domain.model.Order;
import com.orderprocessor.domain.model.PaymentMethod;
import com.orderprocessor.domain.model.Product;
import com.orderprocessor.infrastructure.repository.OrderRepository;
import com.orderprocessor.infrastructure.repository.ProductRepository;

public class OrderServiceTest {
    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private OrderProcessingChain orderProcessingChain;
    private OrderService orderService;

    @Test
    void deveCriarOrderItemQuandoProdutoExiste() {
        productRepository = mock(ProductRepository.class);
        orderRepository = mock(OrderRepository.class);
        orderProcessingChain = mock(OrderProcessingChain.class);

        orderService = new OrderService(productRepository, orderRepository, orderProcessingChain);
        Product produto1 = new Product("Notebook", BigDecimal.valueOf(1), 10);

        CreateOrderItemRequest itemRequest = new CreateOrderItemRequest();
        itemRequest.setProductId(1L);
        itemRequest.setQuantity(2);

        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerName("Cliente teste");
        request.setPaymentMethod(PaymentMethod.PIX);
        request.setItems(List.of(itemRequest));

        when(productRepository.findById(1L)).thenReturn(Optional.of(produto1));
        var resultado = orderService.criarOrderItem(request);

        verify(productRepository).findById(1L);
        assertEquals(1, resultado.size());
        assertEquals(2, resultado.get(0).getQuantity());
        assertEquals("Notebook", resultado.get(0).getProduct().getName());
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExiste() {
        productRepository = mock(ProductRepository.class);
        orderRepository = mock(OrderRepository.class);
        orderProcessingChain = mock(OrderProcessingChain.class);

        orderService = new OrderService(productRepository, orderRepository, orderProcessingChain);

        CreateOrderItemRequest itemRequest = new CreateOrderItemRequest();
        itemRequest.setProductId(1L);
        itemRequest.setQuantity(2);

        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerName("Cliente teste");
        request.setPaymentMethod(PaymentMethod.PIX);
        request.setItems(List.of(itemRequest));

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            orderService.criarOrderItem(request);
        });
    }

    @Test
    void deveCriarOrderQuandoDadosSaoValidos() {
        productRepository = mock(ProductRepository.class);
        orderRepository = mock(OrderRepository.class);
        orderProcessingChain = mock(OrderProcessingChain.class);

        orderService = new OrderService(productRepository, orderRepository, orderProcessingChain);
        Product produto1 = new Product("Notebook", BigDecimal.valueOf(1), 10);
        CreateOrderItemRequest itemRequest = new CreateOrderItemRequest();
        itemRequest.setProductId(1L);
        itemRequest.setQuantity(2);

        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerName("Cliente teste");
        request.setPaymentMethod(PaymentMethod.PIX);
        request.setItems(List.of(itemRequest));

        when(productRepository.findById(1L)).thenReturn(Optional.of(produto1));

        Order order = orderService.criarOrder(request);

        verify(productRepository).findById(1L);
        assertEquals("Cliente teste", order.getCustomerName());
        assertEquals(PaymentMethod.PIX, order.getPaymentMethod());
        assertEquals(1, order.getItems().size());
        assertEquals(2, order.getItems().get(0).getQuantity());
    }

    @Test
    void deveSalvarPedidoMesmoQuandoCadeiaRejeitaPedido() {
        productRepository = mock(ProductRepository.class);
        orderRepository = mock(OrderRepository.class);
        orderProcessingChain = mock(OrderProcessingChain.class);
        OrderHandler orderHandler = mock(OrderHandler.class);

        when(orderProcessingChain.getPrimeiroHandler()).thenReturn(orderHandler);
        when(orderHandler.processar(any(Order.class))).thenReturn(false);

        orderService = new OrderService(productRepository, orderRepository, orderProcessingChain);
        Product produto1 = new Product("Notebook", BigDecimal.valueOf(1), 10);
        CreateOrderItemRequest itemRequest = new CreateOrderItemRequest();
        itemRequest.setProductId(1L);
        itemRequest.setQuantity(2);
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerName("Cliente teste");
        request.setPaymentMethod(PaymentMethod.PIX);
        request.setItems(List.of(itemRequest));
        when(productRepository.findById(1L)).thenReturn(Optional.of(produto1));

        orderService.receberPedido(request);
        verify(orderRepository).save(any(Order.class));
        verify(orderHandler).processar(any(Order.class));
    }
}
