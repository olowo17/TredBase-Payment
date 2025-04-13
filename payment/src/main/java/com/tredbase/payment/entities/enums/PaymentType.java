package com.tredbase.payment.entities.enums;

import lombok.Getter;

@Getter
public enum PaymentType {
    SHARED("SHARED"),
    UNIQUE("UNIQUE");

    private final String paymentType;
    PaymentType(String paymentType){this.paymentType = paymentType;}

}
