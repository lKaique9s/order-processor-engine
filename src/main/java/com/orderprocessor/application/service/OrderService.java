package com.orderprocessor.application.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orderprocessor.application.dto.CreateOrderRequest;
import com.orderprocessor.application.dto.OrderResponse;
import com.orderprocessor.domain.exception.InvalidOrderException;
import com.orderprocessor.domain.exception.ProductNotFoundException;
import com.orderprocessor.domain.handler.OrderHandler;
import com.orderprocessor.domain.handler.OrderProcessingChain;
import com.orderprocessor.domain.model.Order;
import com.orderprocessor.domain.model.OrderItem;
import com.orderprocessor.domain.model.OrderStatus;
import com.orderprocessor.infrastructure.repository.OrderRepository;
import com.orderprocessor.infrastructure.repository.ProductRepository;

@Service
public class OrderService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderProcessingChain orderProcessingChain;

    public OrderService(ProductRepository productRepository, OrderRepository orderRepository,
            OrderProcessingChain orderProcessingChain) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderProcessingChain = orderProcessingChain;
    }

    public List<OrderItem> criarOrderItem(CreateOrderRequest createOrderRequest) {
        if (createOrderRequest == null
                || createOrderRequest.getItems() == null
                || createOrderRequest.getItems().isEmpty()) {
            throw new InvalidOrderException();
        }
        var items = List.copyOf(createOrderRequest.getItems());
        List<OrderItem> listOrderItems = new ArrayList<>();

        for (var item : items) {
            var product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException());
            listOrderItems.add(new OrderItem(product, item.getQuantity()));
        }

        return listOrderItems;
    }

    public Order criarOrder(CreateOrderRequest createOrderRequest) {
        var listOrderItems = criarOrderItem(createOrderRequest);

        return new Order(createOrderRequest.getCustomerName(), listOrderItems, createOrderRequest.getPaymentMethod());
    }

    public OrderResponse processarPedido(Order order) {
        OrderHandler primeiroHandler = orderProcessingChain.getPrimeiroHandler();
        boolean processado = primeiroHandler.processar(order);

        if (!processado && order.getStatus() == OrderStatus.PROCESSING)
            order.reject();

        orderRepository.save(order);
        return new OrderResponse(order);
    }

    @Transactional
    public OrderResponse receberPedido(CreateOrderRequest createOrderRequest) {
        Order order = criarOrder(createOrderRequest);
        return processarPedido(order);
    }

}
