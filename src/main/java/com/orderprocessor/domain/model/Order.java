package com.orderprocessor.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private Long id;
    private List<OrderItem> items;
    private String customerName;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private LocalDateTime createdAt;

    public Order(String customerName, List<OrderItem> items) {
        this.items = new ArrayList<>(items);
        this.customerName = customerName;
        this.createdAt = LocalDateTime.now();
        this.totalAmount = calculaTotalAmount();
        this.status = OrderStatus.CREATED;
    }

    public BigDecimal calculaTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : items) {
            var result = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(result);
        }
        return total;
    }

    public void addItem(OrderItem item) {
        if (item == null)
            throw new IllegalArgumentException();
        
        items.add(item);
        var newItemValue = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        totalAmount = totalAmount.add(newItemValue);
    }

    public void removeItem(OrderItem item) {
        if (items.remove(item))
            this.totalAmount = calculaTotalAmount();
    }

    public Long getId() {
        return id;
    }

    public List<OrderItem> getItems() {
        return List.copyOf(items);
    }

    public String getCustomerName() {
        return customerName;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    
}
