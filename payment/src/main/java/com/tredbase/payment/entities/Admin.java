package com.tredbase.payment.entities;

import com.tredbase.payment.entities.enums.Role;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
public class Admin extends BaseUserDetails {
    public Admin() {
        setRole(Role.ROLE_ADMIN);
    }
}
