package com.danvinicius.simplifiedpicpay.exceptions;

public class UnauthorizedTransaction extends RuntimeException {
    public UnauthorizedTransaction() {
        super("Unauthorized transaction");
    }

    public UnauthorizedTransaction(String msg) {
        super(msg);
    }
}
