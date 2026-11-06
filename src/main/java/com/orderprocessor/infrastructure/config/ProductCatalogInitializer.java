package com.orderprocessor.infrastructure.config;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.orderprocessor.domain.model.Product;
import com.orderprocessor.infrastructure.catalog.ProductCatalog;

@Component 
public class ProductCatalogInitializer implements CommandLineRunner{

    private final ProductCatalog productCatalog;

    @Override 
    public void run(String... args) throws Exception{
        Product produto1 = new Product("Notebook", BigDecimal.valueOf(3000), 10);
        Product produto2 = new Product("Mouse", BigDecimal.valueOf(100), 20);
        productCatalog.cadastrarProduto(1L, produto1);
        productCatalog.cadastrarProduto(2L, produto2);
    }

    public ProductCatalogInitializer(ProductCatalog productCatalog){
        this.productCatalog = productCatalog;
    }
    
}
