package com.revature.nile.exceptions;

public class OnlyOneReviewPerUserException extends RuntimeException {
    public OnlyOneReviewPerUserException(String s) {
        super(s);
    }
}
