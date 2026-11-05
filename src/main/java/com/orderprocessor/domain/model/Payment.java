package com.orderprocessor.domain.model;

import java.math.BigDecimal;

public class Payment {
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private BigDecimal amount;

    public Payment(PaymentMethod paymentMethod, BigDecimal amount){
        if(paymentMethod == null 
            || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException();

        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.paymentStatus = PaymentStatus.PENDING;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void approve(){
        this.paymentStatus = PaymentStatus.APPROVED;
    }

    public void reject(){
        this.paymentStatus = PaymentStatus.REJECTED;
    }

    
}
