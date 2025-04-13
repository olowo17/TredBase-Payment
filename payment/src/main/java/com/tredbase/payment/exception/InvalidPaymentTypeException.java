package com.tredbase.payment.exception;

public class InvalidPaymentTypeException extends RuntimeException {
    public InvalidPaymentTypeException(String message) {
        super(message);
    }
}
