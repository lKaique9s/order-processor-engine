package com.orderprocessor.domain.handler;

import com.orderprocessor.domain.model.Order;

public class StockValidationHandler implements OrderHandler {
    private OrderHandler nextHandler;

    public boolean processar(Order order) {
        if (order == null)
            return false;
        var items = order.getItems();

        // valida se todos os items tem stock necessario
        for (var item : items) {
            if (item.getQuantity() == null
                    || item.getProduct() == null
                    || item.getProduct().getStockQuantity() <= 0
                    || item.getQuantity() > item.getProduct().getStockQuantity()) {
                order.reject();
                return false;
            }

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
