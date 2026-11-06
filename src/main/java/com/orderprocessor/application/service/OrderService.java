package com.orderprocessor.application.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.orderprocessor.application.dto.CreateOrderRequest;
import com.orderprocessor.application.dto.OrderResponse;
import com.orderprocessor.domain.exception.ProductNotFoundException;
import com.orderprocessor.domain.handler.OrderHandler;
import com.orderprocessor.domain.handler.OrderProcessingChain;
import com.orderprocessor.domain.model.Order;
import com.orderprocessor.domain.model.OrderItem;
import com.orderprocessor.infrastructure.catalog.ProductCatalog;

@Service
public class OrderService {
    private final ProductCatalog productCatalog;
    private final OrderProcessingChain orderProcessingChain;

    public OrderService(ProductCatalog productCatalog, OrderProcessingChain orderProcessingChain) {
        this.productCatalog = productCatalog;
        this.orderProcessingChain = orderProcessingChain;
    }

    public List<OrderItem> criarOrderItem(CreateOrderRequest createOrderRequest) {
        var items = List.copyOf(createOrderRequest.getItems());
        List<OrderItem> listOrderItems = new ArrayList<>();

        for (var item : items) {
            var product = productCatalog.findById(item.getProductId());
            if (product == null)
                throw new ProductNotFoundException();
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
        primeiroHandler.processar(order);
        return new OrderResponse(order);
    }

    public OrderResponse receberPedido(CreateOrderRequest createOrderRequest) {
        Order order = criarOrder(createOrderRequest);
        return processarPedido(order);
    }

}
