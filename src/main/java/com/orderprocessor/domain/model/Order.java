package com.orderprocessor.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<OrderItem> items;
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "payment_id", nullable = true)
    private Payment payment;
    @Column(nullable = false)
    private String customerName;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal discountAmount;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal shippingAmount;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    protected Order() {
    }

    public Order(String customerName, List<OrderItem> items, PaymentMethod paymentMethod) {
        this.items = new ArrayList<>(items);
        associaItemToOrder(this.items);
        this.customerName = customerName;
        this.createdAt = LocalDateTime.now();
        this.totalAmount = calculaTotalAmount();
        this.status = OrderStatus.CREATED;
        this.discountAmount = BigDecimal.ZERO;
        this.shippingAmount = BigDecimal.ZERO;
        setPaymentMethod(paymentMethod);
    }

    public BigDecimal calculaTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : items) {
            var result = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(result);
        }
        return total;
    }

    public BigDecimal calculaFinalAmount() {
        BigDecimal total = BigDecimal.ZERO;
        total = totalAmount.add(shippingAmount);
        total = total.subtract(discountAmount);
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    public void addItem(OrderItem item) {
        if (item == null)
            throw new IllegalArgumentException();

        items.add(item);
        var newItemValue = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        totalAmount = totalAmount.add(newItemValue);
        item.setOrder(this);
    }

    public void removeItem(OrderItem item) {
        if (items.remove(item)) {
            this.totalAmount = calculaTotalAmount();
            item.removeOrderReference();
        }
    }

    public Long getId() {
        return id;
    }

    public List<OrderItem> getItems() {
        return List.copyOf(items);
    }

    public String getCustomerName() {
        return customerName;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        if (discountAmount == null || discountAmount.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException();
        this.discountAmount = discountAmount.setScale(2, RoundingMode.HALF_UP);

    }

    public BigDecimal getShippingAmount() {
        return shippingAmount;
    }

    public void setShippingAmount(BigDecimal shippingAmount) {
        if (shippingAmount == null || shippingAmount.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException();
        this.shippingAmount = shippingAmount.setScale(2, RoundingMode.HALF_UP);
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        if (payment == null)
            throw new IllegalArgumentException();
        this.payment = payment;
        payment.setOrder(this);
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        if (paymentMethod != PaymentMethod.CREDIT_CARD
                && paymentMethod != PaymentMethod.DEBIT_CARD
                && paymentMethod != PaymentMethod.PIX)
            throw new IllegalArgumentException();
        this.paymentMethod = paymentMethod;
    }

    public void confirm() {
        if (status != OrderStatus.PROCESSING)
            throw new IllegalArgumentException();
        this.status = OrderStatus.CONFIRMED;
    }

    public void reject() {
        if (status != OrderStatus.PROCESSING)
            throw new IllegalArgumentException();
        this.status = OrderStatus.REJECTED;
    }

    public void process() {
        if (status != OrderStatus.CREATED)
            throw new IllegalArgumentException();
        this.status = OrderStatus.PROCESSING;
    }

    private void associaItemToOrder(List<OrderItem> items) {
        if (items == null)
            throw new IllegalArgumentException();
        for (var item : items) {
            item.setOrder(this);
        }
    }

}
