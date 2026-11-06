package com.orderprocessor.controller;

import com.orderprocessor.infrastructure.repository.ProductRepository;

import org.springframework.web.bind.annotation.RestController;

import com.orderprocessor.domain.exception.ProductNotFoundException;
import com.orderprocessor.domain.model.Product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController 
@RequestMapping("api/products")
public class ProductController {
    
    private final ProductRepository productRepository;

    ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/{id}")
    public Product findProduct(@PathVariable Long id) {
        return productRepository.findById(id)
                    .orElseThrow(() -> new ProductNotFoundException());
    }
    
}
