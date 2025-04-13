package com.tredbase.payment.entities.enums;


import lombok.Getter;

@Getter
public enum PaymentStatus {
    SUCCESS("SUCCESS"),
    FAILED("FAILED"),
    PENDING("PENDING");

    private final String paymentStatus;
    PaymentStatus(String paymentStatus){this.paymentStatus = paymentStatus;}

}
