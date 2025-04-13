package com.tredbase.payment.exception;

public class ParentNotFoundException extends RuntimeException {
    public ParentNotFoundException() {
        super("Parent not found");
    }
}
