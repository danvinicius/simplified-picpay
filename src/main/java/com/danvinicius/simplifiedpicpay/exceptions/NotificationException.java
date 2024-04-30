package com.danvinicius.simplifiedpicpay.exceptions;

public class NotificationException extends RuntimeException {
    public NotificationException(String msg) {
        super(msg);
    }

    public NotificationException() {
        super("Notification service is down");
    }
}
