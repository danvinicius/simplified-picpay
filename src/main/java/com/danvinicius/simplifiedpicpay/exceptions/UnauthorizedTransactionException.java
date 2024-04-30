package com.danvinicius.simplifiedpicpay.exceptions;

public class UnauthorizedTransactionException extends RuntimeException {
    public UnauthorizedTransactionException() {
        super("Unauthorized transaction");
    }

    public UnauthorizedTransactionException(String msg) {
        super(msg);
    }
}
