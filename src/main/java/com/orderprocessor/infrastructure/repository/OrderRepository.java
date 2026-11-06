package com.orderprocessor.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.orderprocessor.domain.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
}
