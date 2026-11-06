package com.orderprocessor.infrastructure.catalog;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.orderprocessor.domain.model.Product;

@Component 
public class ProductCatalog {
    private Map<Long, Product> productCatalog = new HashMap<>();


    public Product findById(Long id){
        return productCatalog.get(id);
    }

    public void cadastrarProduto(Long id, Product produto){
        productCatalog.put(id, produto);
    }
}
