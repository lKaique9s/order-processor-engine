package com.orderprocessor.service;

public class NoStockQuantityException extends RuntimeException{

    public NoStockQuantityException(){
        super("Sem Estoque Disponivel");
    }

    public NoStockQuantityException(String mensagem){
        super(mensagem);
    }
    
}
