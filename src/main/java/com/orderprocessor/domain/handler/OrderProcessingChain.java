package com.orderprocessor.domain.handler;

import org.springframework.stereotype.Component;

@Component
public class OrderProcessingChain {
    private final OrderValidationHandler orderValidationHandler;
    private final ProductValidationHandler productValidationHandler;
    private final StockValidationHandler stockValidationHandler;
    private final DiscountHandler discountHandler;
    private final ShippingHandler shippingHandler;
    private final PaymentHandler paymentHandler;
    private final OrderConfirmationHandler orderConfirmationHandler;

    public OrderProcessingChain(
            OrderValidationHandler orderValidationHandler,
            ProductValidationHandler productValidationHandler,
            StockValidationHandler stockValidationHandler,
            DiscountHandler discountHandler,
            ShippingHandler shippingHandler,
            PaymentHandler paymentHandler,
            OrderConfirmationHandler orderConfirmationHandler) {
        this.orderValidationHandler = orderValidationHandler;
        this.productValidationHandler = productValidationHandler;
        this.stockValidationHandler = stockValidationHandler;
        this.discountHandler = discountHandler;
        this.shippingHandler = shippingHandler;
        this.paymentHandler = paymentHandler;
        this.orderConfirmationHandler = orderConfirmationHandler;
        orderValidationHandler.setNextHandler(productValidationHandler);
        productValidationHandler.setNextHandler(stockValidationHandler);
        stockValidationHandler.setNextHandler(discountHandler);
        discountHandler.setNextHandler(shippingHandler);
        shippingHandler.setNextHandler(paymentHandler);
        paymentHandler.setNextHandler(orderConfirmationHandler);
    }

    public OrderHandler getPrimeiroHandler() {
        return orderValidationHandler;
    }
}
