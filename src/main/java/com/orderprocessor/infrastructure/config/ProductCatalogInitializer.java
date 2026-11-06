package com.orderprocessor.infrastructure.config;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.orderprocessor.domain.model.Product;
import com.orderprocessor.infrastructure.repository.ProductRepository;

@Component
public class ProductCatalogInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() == 0) {
            Product produto1 = new Product("Notebook", BigDecimal.valueOf(3000), 10);
            Product produto2 = new Product("Mouse", BigDecimal.valueOf(100), 20);
            productRepository.save(produto1);
            productRepository.save(produto2);
        }

    }

    public ProductCatalogInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

}
