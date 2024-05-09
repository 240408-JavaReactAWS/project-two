package com.revature.nile.exceptions;

public class ItemNotFoundException extends RuntimeException{
    public ItemNotFoundException(String s) {
        super(s);
    }
}
