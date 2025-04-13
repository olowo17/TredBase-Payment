package com.tredbase.payment.repository;

import com.tredbase.payment.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository <Payment,Long> {
}
