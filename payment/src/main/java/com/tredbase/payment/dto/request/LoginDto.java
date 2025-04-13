package com.tredbase.payment.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Getter
public class LoginDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -742058309253700795L;

    @Email(message = "please enter a valid email")
    @NotBlank(message = "email must not be blank")
    private String email;

    @NotBlank(message = "password must not be blank")
    private String password;

}
