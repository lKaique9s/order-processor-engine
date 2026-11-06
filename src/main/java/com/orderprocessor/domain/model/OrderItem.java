package com.orderprocessor.domain.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity 
@Table (name = "order_item")
public class OrderItem {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (nullable = false)
    private Long id;
    @ManyToOne 
    @JoinColumn (name = "product_id", nullable = false)
    private Product product;
    @ManyToOne 
    @JoinColumn (name = "order_id", nullable = false)
    private Order order;
    @Column (nullable = false)
    private Integer quantity;
    @Column (nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    protected OrderItem(){}

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


    public void setOrder(Order order){
        if(order == null)
            throw new IllegalArgumentException();
        this.order = order;
    }

    public void removeOrderReference(){
        this.order = null;
    }
}
