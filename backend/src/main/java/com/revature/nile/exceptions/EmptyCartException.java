package com.revature.nile.exceptions;

public class EmptyCartException extends RuntimeException{
    public EmptyCartException(String s){
        super(s);
    }
}
