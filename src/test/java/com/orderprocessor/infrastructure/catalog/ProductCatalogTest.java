
package com.orderprocessor.infrastructure.catalog;

import java.math.BigDecimal;

import com.orderprocessor.domain.model.Product;

public class ProductCatalogTest {

    public static void main(String[] args) {

        ProductCatalog catalog = new ProductCatalog();

        Product notebook = new Product(
            "Notebook",
            new BigDecimal("3000.00"),
            10
        );

        catalog.cadastrarProduto(1L, notebook);

        Product productFound = catalog.findById(1L);

        System.out.println(productFound.getName());
    }
}
