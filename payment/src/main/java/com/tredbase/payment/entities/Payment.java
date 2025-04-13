package com.tredbase.payment.entities;

import com.tredbase.payment.entities.enums.PaymentStatus;
import com.tredbase.payment.entities.enums.PaymentType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal originalAmount;
    private BigDecimal adjustedAmount;
    private BigDecimal serviceFee;


    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Column(nullable = false, unique = true, updatable = false)
    private String transactionReference;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = false)
    private Parent parent;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @PrePersist
    public void onCreate() {
        this.timestamp = LocalDateTime.now();
        if (this.transactionReference == null) {
            this.transactionReference = UUID.randomUUID().toString();
        }
    }

}
