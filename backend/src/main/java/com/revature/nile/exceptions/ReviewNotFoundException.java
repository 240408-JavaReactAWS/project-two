package com.revature.nile.exceptions;

public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(String s) {
        super(s);
    }
}
