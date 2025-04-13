package com.tredbase.payment.dto.response;

import com.tredbase.payment.entities.enums.PaymentStatus;
import com.tredbase.payment.entities.enums.PaymentType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentResponse {
    private String transactionReference;
    private BigDecimal originalAmount;
    private BigDecimal serviceFee;
    private BigDecimal adjustedAmount;
    private PaymentStatus paymentStatus;
    private PaymentType paymentType;
    private LocalDateTime timestamp;

}
