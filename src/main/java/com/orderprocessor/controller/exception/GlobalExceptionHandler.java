package com.orderprocessor.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.orderprocessor.domain.exception.NoStockQuantityException;
import com.orderprocessor.domain.exception.ProductNotFoundException;

@RestControllerAdvice 
public class GlobalExceptionHandler{
    

    @ExceptionHandler(NoStockQuantityException.class)
    public ResponseEntity<String> handlerNoStockQuantity(NoStockQuantityException ex){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(ex.getMessage());
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handlerProductNotFound(ProductNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
