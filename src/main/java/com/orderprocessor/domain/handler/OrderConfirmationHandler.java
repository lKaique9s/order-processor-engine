package com.orderprocessor.domain.handler;

import org.springframework.stereotype.Component;

import com.orderprocessor.domain.model.Order;
import com.orderprocessor.domain.model.OrderStatus;
import com.orderprocessor.domain.model.PaymentStatus;

@Component 
public class OrderConfirmationHandler implements OrderHandler {
    private OrderHandler nextHandler;

    public boolean processar(Order order) {
        if (order == null
                || order.getPayment() == null
                || order.getPayment().getPaymentStatus() == null
                || order.getStatus() == null
                || order.getStatus() != OrderStatus.PROCESSING)
            return false;
            
        if(order.getPayment().getPaymentStatus() != PaymentStatus.APPROVED){
            order.reject();
            return false;
        }
            


        //re-verifica se tem estoque e retira do estoque os produtos comprados
        var items = order.getItems();
        for (var item : items) {
            if (item.getQuantity() > item.getProduct().getStockQuantity()){
                order.reject();
                return false;
            }
        }
        for (var item : items) {
            item.getProduct().removeStock(item.getQuantity());
        }

        order.confirm();
        if (order.getStatus() == OrderStatus.CONFIRMED) {
            if (nextHandler == null)
                return true;
            return nextHandler.processar(order);
        }
        return false;

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
