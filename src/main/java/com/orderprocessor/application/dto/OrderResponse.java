package com.orderprocessor.application.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.orderprocessor.domain.model.Order;
import com.orderprocessor.domain.model.OrderItem;
import com.orderprocessor.domain.model.OrderStatus;
import com.orderprocessor.domain.model.PaymentMethod;

public class OrderResponse {
    private String customerName;
    private List<OrderItemResponse> items;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private BigDecimal shippingAmount;
    private OrderStatus status;
    private PaymentMethod paymentMethod;
    private BigDecimal finalAmount;

    public OrderResponse(Order order) {
        this.customerName = order.getCustomerName();
        this.items = convertItems(order.getItems());
        this.totalAmount = order.getTotalAmount();
        this.discountAmount = order.getDiscountAmount();
        this.shippingAmount = order.getShippingAmount();
        this.status = order.getStatus();
        this.paymentMethod = order.getPaymentMethod();
        this.finalAmount = order.calculaFinalAmount();
    }

    private List<OrderItemResponse> convertItems(List<OrderItem> items) {
        List<OrderItemResponse> lista = new ArrayList<>();
        for (var item : items) {
            lista.add(new OrderItemResponse(item));
        }
        return lista;
    }

    public String getCustomerName() {
        return customerName;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public BigDecimal getShippingAmount() {
        return shippingAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }

}
