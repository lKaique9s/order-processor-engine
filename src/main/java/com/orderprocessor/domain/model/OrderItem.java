package com.orderprocessor.domain.model;

import java.math.BigDecimal;

public class OrderItem {
    private Product product;
    private Integer quantity;
    private BigDecimal unitPrice;

    public OrderItem(Product product, Integer quantity){
        if(product == null || quantity == null || quantity <= 0)
            throw new IllegalArgumentException();

        this.product = product;
        this.quantity = quantity;
        this.unitPrice = product.getPrice();
    }

    public Product getProduct() {
        return product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

}
