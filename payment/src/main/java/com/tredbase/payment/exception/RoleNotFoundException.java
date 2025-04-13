package com.tredbase.payment.exception;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String s) {
        super("Role not found");
    }
}
