package com.tredbase.payment.dto.request;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentDto {
    private BigDecimal paymentAmount;
    private Long parentId;
    private Long studentId;

}
