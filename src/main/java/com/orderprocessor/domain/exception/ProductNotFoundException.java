package com.orderprocessor.domain.exception;

public class ProductNotFoundException extends RuntimeException{
    
    public ProductNotFoundException(){
        super("Produto nao foi encontrado!");
    }

    public ProductNotFoundException(String mensagem){
        super(mensagem);
    }
}
