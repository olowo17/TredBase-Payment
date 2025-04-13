package com.tredbase.payment.entities.enums;

import lombok.Getter;

@Getter
public enum Role {
    ROLE_STUDENT("ROLE_STUDENT"),
    ROLE_PARENT("ROLE_PARENT"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String role;
    Role(String role){this.role=role;}

}
