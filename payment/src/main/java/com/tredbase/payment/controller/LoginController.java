package com.tredbase.payment.controller;

import com.tredbase.payment.dto.request.LoginDto;
import com.tredbase.payment.dto.response.AuthenticationResponse;
import com.tredbase.payment.dto.response.BaseResponse;
import com.tredbase.payment.dto.response.ResponseCodes;
import com.tredbase.payment.exception.UnAuthorizedException;
import com.tredbase.payment.service.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<AuthenticationResponse>> login(@Valid @RequestBody LoginDto loginRequest) {

        try {
            AuthenticationResponse authenticationResponse = loginService.loginUser(loginRequest);
            BaseResponse<AuthenticationResponse> response = new BaseResponse<>(
                    ResponseCodes.SUCCESS,
                    ResponseCodes.LOGIN_SUCCESS,
                    authenticationResponse
            );
            return ResponseEntity.ok(response);

        } catch (UnAuthorizedException e) {
            BaseResponse<AuthenticationResponse> response = new BaseResponse<>(
                    ResponseCodes.ERROR,
                    e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        }
    }

}
