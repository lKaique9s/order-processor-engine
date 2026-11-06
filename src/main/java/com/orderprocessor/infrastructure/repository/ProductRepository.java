package com.orderprocessor.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orderprocessor.domain.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
}
