package com.orderprocessor.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import com.orderprocessor.infrastructure.repository.ProductRepository;

import org.springframework.web.bind.annotation.RestController;

import com.orderprocessor.domain.exception.ProductNotFoundException;
import com.orderprocessor.domain.model.Product;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Products", description = "Endpoints for product management")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Operation(summary = "Find product by ID", description = "Retrieves a product using its unique identifier")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product successfully reached"),
            @ApiResponse(responseCode = "404", description = "Product Not Found")
    })
    @GetMapping("/{id}")
    public Product findProduct(@PathVariable Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException());
    }

}
