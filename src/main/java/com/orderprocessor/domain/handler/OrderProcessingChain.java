package com.orderprocessor.domain.handler;

public class OrderProcessingChain {
    private OrderValidationHandler orderValidationHandler = new OrderValidationHandler();
    private ProductValidationHandler productValidationHandler = new ProductValidationHandler();
    private StockValidationHandler stockValidationHandler = new StockValidationHandler();


    public OrderProcessingChain(){
        orderValidationHandler.setNextHandler(productValidationHandler);
        productValidationHandler.setNextHandler(stockValidationHandler);
    }

    public OrderHandler getPrimeiroHandler(){
        return orderValidationHandler;
    }
}
