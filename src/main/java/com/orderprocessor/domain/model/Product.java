package com.orderprocessor.domain.model;

import java.math.BigDecimal;

import com.orderprocessor.domain.exception.NoStockQuantityException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity 
@Table (name = "products")
public class Product {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (nullable = false)
    private Long id;
    @Column (nullable = false)
    private String name;
    @Column (precision = 12, scale = 2, nullable = false)
    private BigDecimal price;
    @Column (name= "stock_quantity", nullable = false)
    private int stockQuantity;
    @Version 
    @Column (nullable = false)
    private Long version;

    protected Product(){}

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
