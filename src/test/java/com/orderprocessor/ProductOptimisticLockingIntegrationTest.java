package com.orderprocessor;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;

import com.orderprocessor.domain.model.Product;

import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;

@SpringBootTest
public class ProductOptimisticLockingIntegrationTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Test
    void deveDetectarConflitosVersao() {
        Product produto = transactionTemplate.execute(status -> {
            Product novoProduto = new Product(
                    "Produto Teste Concorrencia",
                    new BigDecimal("100.00"),
                    10);

            entityManager.persist(novoProduto);
            entityManager.flush();
            return novoProduto;
        });

        Product produtoDesatualizado = transactionTemplate.execute(status -> {
            return entityManager.find(Product.class, produto.getId());
        });

        transactionTemplate.execute(status -> {
            Product produtoAtual = entityManager.find(Product.class, produto.getId());
            produtoAtual.removeStock(1);
            entityManager.flush();
            return null;
        });

        assertThrows(OptimisticLockException.class, () -> transactionTemplate.execute(status -> {
            produtoDesatualizado.removeStock(1);

            entityManager.merge(produtoDesatualizado);
            entityManager.flush();

            return null;
        }));
    }
}
