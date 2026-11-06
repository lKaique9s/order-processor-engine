package com.orderprocessor.controller;

import com.orderprocessor.infrastructure.catalog.ProductCatalog;
import org.springframework.web.bind.annotation.RestController;

import com.orderprocessor.domain.model.Product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController 
@RequestMapping("api/products")
public class ProductController {
    
    private final ProductCatalog productCatalog;

    ProductController(ProductCatalog productCatalog) {
        this.productCatalog = productCatalog;
    }

    @GetMapping("/{id}")
    public Product findProduct(@PathVariable Long id) {
        return productCatalog.findById(id);
    }
    
}
