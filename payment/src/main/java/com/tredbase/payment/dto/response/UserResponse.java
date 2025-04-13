package com.tredbase.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tredbase.payment.entities.enums.PaymentType;
import com.tredbase.payment.entities.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private Role role;

}
