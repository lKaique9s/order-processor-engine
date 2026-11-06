package com.orderprocessor.domain.handler;

import org.springframework.stereotype.Component;

@Component 
public class OrderProcessingChain {
    private OrderValidationHandler orderValidationHandler = new OrderValidationHandler();
    private ProductValidationHandler productValidationHandler = new ProductValidationHandler();
    private StockValidationHandler stockValidationHandler = new StockValidationHandler();
    private DiscountHandler discountHandler = new DiscountHandler();
    private ShippingHandler shippingHandler = new ShippingHandler();
    private PaymentHandler paymentHandler = new PaymentHandler();
    private OrderConfirmationHandler orderConfirmationHandler = new OrderConfirmationHandler();


    public OrderProcessingChain(){
        orderValidationHandler.setNextHandler(productValidationHandler);
        productValidationHandler.setNextHandler(stockValidationHandler);
        stockValidationHandler.setNextHandler(discountHandler);
        discountHandler.setNextHandler(shippingHandler);
        shippingHandler.setNextHandler(paymentHandler);
        paymentHandler.setNextHandler(orderConfirmationHandler);
    }

    public OrderHandler getPrimeiroHandler(){
        return orderValidationHandler;
    }
}
