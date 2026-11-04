package com.orderprocessor.domain.handler;

import java.math.BigDecimal;

import com.orderprocessor.domain.model.Order;

public class ProductValidationHandler implements OrderHandler {
    private OrderHandler nextHandler;

    public boolean processar(Order order) {
        if (order == null)
            return false;

        var items = order.getItems();
        //valida produtos
        for (var item : items) {
            if (item.getProduct() == null
                    || item.getProduct().getName() == null
                    || item.getProduct().getName().isBlank()
                    || item.getProduct().getPrice() == null
                    || item.getProduct().getPrice().compareTo(BigDecimal.ZERO) <= 0
                    || item.getQuantity() == null
                    || item.getQuantity() <= 0
                    || item.getUnitPrice() == null
                    || item.getUnitPrice().compareTo(BigDecimal.ZERO) <= 0)
                return false;
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
