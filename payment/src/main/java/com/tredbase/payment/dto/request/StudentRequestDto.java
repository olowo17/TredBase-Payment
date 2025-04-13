package com.tredbase.payment.dto.request;

import com.tredbase.payment.entities.enums.PaymentType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class StudentRequestDto extends BaseUserRequestDto {

    @NotNull(message = "PaymentType must not be null for students")
    private PaymentType paymentType;

    private List<Long> parentIds;


}
