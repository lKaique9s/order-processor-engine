package com.orderprocessor.domain.exception;

public class InvalidOrderException extends RuntimeException{

    public InvalidOrderException(){
        super("Os dados sao invalidos!");
    }

    public InvalidOrderException(String mensagem){
        super(mensagem);
    }
}
