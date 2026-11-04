package com.orderprocessor.domain.model;

import java.math.BigDecimal;

import com.orderprocessor.service.NoStockQuantityException;

public class Product {
    private Long id;
    private String name;
    private BigDecimal price;
    private int stockQuantity;

    public Product(String name, BigDecimal price, int stockQuantity) {
        if( name == null || name.isBlank() 
        || price == null 
        || price.compareTo(BigDecimal.ZERO) <= 0 
        || stockQuantity < 0)
            throw new IllegalArgumentException();
    
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setName(String name) {
        if( name == null || name.isBlank())
            throw new IllegalArgumentException();
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        if(price == null || price.compareTo(BigDecimal.ZERO) <= 0 )
            throw new IllegalArgumentException();
        this.price = price;
    }

    public void addStock(int quantity){
        if(quantity <= 0 )
            throw new IllegalArgumentException();
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity){
        if(quantity <= 0 )
            throw new IllegalArgumentException();
        if(quantity > this.stockQuantity)
            throw new NoStockQuantityException();
        this.stockQuantity -= quantity;
    }


}
