package com.orderprocessor.domain.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity 
@Table (name = "payment")
public class Payment {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (nullable = false)
    private Long id;
    @Enumerated (EnumType.STRING)
    @Column (nullable = false)
    private PaymentMethod paymentMethod;
    @Enumerated (EnumType.STRING)
    @Column (nullable = false)
    private PaymentStatus paymentStatus;
    @Column (nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;
    @OneToOne (mappedBy = "payment")
    private Order order;

    protected Payment(){}

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

    public void setOrder(Order order){
        if(order == null)
            throw new IllegalArgumentException();
        this.order = order;
    }
    
}
