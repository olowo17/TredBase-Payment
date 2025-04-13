package com.tredbase.payment.entities;

import com.tredbase.payment.entities.enums.PaymentType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
public class Student extends BaseUserDetails{
    @ManyToMany(mappedBy = "students")
    private Set<Parent> parents = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType = PaymentType.UNIQUE;;

    private BigDecimal balance = BigDecimal.ZERO;

}
