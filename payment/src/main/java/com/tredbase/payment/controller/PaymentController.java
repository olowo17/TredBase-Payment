package com.tredbase.payment.controller;

import com.tredbase.payment.dto.request.PaymentDto;
import com.tredbase.payment.dto.response.BaseResponse;
import com.tredbase.payment.dto.response.PaymentResponse;
import com.tredbase.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<BaseResponse<PaymentResponse>> processPayment(@RequestBody @Valid PaymentDto paymentDto) {
        BaseResponse<PaymentResponse> response = paymentService.processPayment(paymentDto);
        return ResponseEntity.ok(response);
    }
}
