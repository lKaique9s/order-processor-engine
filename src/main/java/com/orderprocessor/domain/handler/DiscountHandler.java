package com.orderprocessor.domain.handler;

import java.math.BigDecimal;

import com.orderprocessor.domain.model.Order;

public class DiscountHandler implements OrderHandler {
    private OrderHandler nextHandler;

    public boolean processar(Order order) {
        if (order == null
                || order.getTotalAmount() == null
                || order.getTotalAmount().compareTo(BigDecimal.ZERO) < 0)
            return false;

        BigDecimal discountAmount = order.getTotalAmount();
        if (discountAmount.compareTo(BigDecimal.valueOf(500)) > 0) {
            discountAmount = discountAmount.multiply(BigDecimal.valueOf(0.1));
            order.setDiscountAmount(discountAmount);
        } else {
            discountAmount = discountAmount.multiply(BigDecimal.valueOf(0.03));
            order.setDiscountAmount(discountAmount);
        }
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
