package com.orderprocessor.domain.handler;

import java.math.BigDecimal;
import org.springframework.stereotype.Component;
import com.orderprocessor.domain.model.Order;

@Component 
public class ShippingHandler implements OrderHandler {
    private OrderHandler nextHandler;

    public boolean processar(Order order) {
        if (order == null 
            || order.getTotalAmount() == null
            || order.getTotalAmount().compareTo(BigDecimal.ZERO) < 0)
            return false;
        BigDecimal shippingAmount = BigDecimal.ZERO;

        if (order.getTotalAmount().compareTo(BigDecimal.valueOf(100)) <= 0) {
            shippingAmount = BigDecimal.valueOf(20);
        } else if (order.getTotalAmount().compareTo(BigDecimal.valueOf(500)) <= 0) {
            shippingAmount = BigDecimal.valueOf(15);
        }
        order.setShippingAmount(shippingAmount);

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
