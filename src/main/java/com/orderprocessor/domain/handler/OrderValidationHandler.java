package com.orderprocessor.domain.handler;

import org.springframework.stereotype.Component;

import com.orderprocessor.domain.exception.InvalidOrderException;
import com.orderprocessor.domain.model.Order;

@Component 
public class OrderValidationHandler implements OrderHandler {
    private OrderHandler nextHandler;


    public boolean processar(Order order) {
        //valida pedidos
        if (order == null
                || order.getCustomerName() == null
                || order.getCustomerName().isBlank()
                || order.getItems() == null
                || order.getItems().isEmpty())
            throw new InvalidOrderException();

        order.process();

        if (nextHandler == null)
            return true;

        return nextHandler.processar(order);
    }

    public void setNextHandler(OrderHandler handler) {
        if (handler == null)
            throw new IllegalArgumentException();
        this.nextHandler = handler;
    }

    public OrderHandler getNextHandler() {
        return nextHandler;
    }

}
