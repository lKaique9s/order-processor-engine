package com.orderprocessor.domain.handler;

import java.math.BigDecimal;

import com.orderprocessor.domain.model.Order;
import com.orderprocessor.domain.model.Payment;
import com.orderprocessor.domain.model.PaymentMethod;
import com.orderprocessor.domain.model.PaymentStatus;

public class PaymentHandler implements OrderHandler {
    private OrderHandler nextHandler;

    public boolean processar(Order order) {
        if (order == null
                || order.getPaymentMethod() == null
                || order.calculaFinalAmount() == null
                || order.calculaFinalAmount().compareTo(BigDecimal.ZERO) <= 0)
            return false;

        Payment payment = new Payment(order.getPaymentMethod(), order.calculaFinalAmount());
        order.setPayment(payment);

        if (payment.getPaymentStatus() == PaymentStatus.PENDING) {
            if (payment.getPaymentMethod() == PaymentMethod.PIX) {
                payment.approve();
            } else if (payment.getPaymentMethod() == PaymentMethod.DEBIT_CARD) {
                payment.approve();
            } else if (payment.getPaymentMethod() == PaymentMethod.CREDIT_CARD) {
                if (payment.getAmount().compareTo(BigDecimal.valueOf(5000)) <= 0) {
                    payment.approve();
                } else {
                    payment.reject();
                }
            }
        }

        if (payment.getPaymentStatus() == PaymentStatus.REJECTED)
            order.reject();

        if (payment.getPaymentStatus() == PaymentStatus.APPROVED) {
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
