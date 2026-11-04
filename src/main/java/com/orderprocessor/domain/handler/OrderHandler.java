package com.orderprocessor.domain.handler;

import com.orderprocessor.domain.model.Order;

public interface OrderHandler {
    boolean processar(Order order);
    
}