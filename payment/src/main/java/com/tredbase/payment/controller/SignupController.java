package com.tredbase.payment.controller;

import com.tredbase.payment.dto.request.AdminRequestDto;
import com.tredbase.payment.dto.request.ParentRequestDto;
import com.tredbase.payment.dto.request.StudentRequestDto;
import com.tredbase.payment.dto.response.BaseResponse;
import com.tredbase.payment.dto.response.UserResponse;
import com.tredbase.payment.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/register")
public class SignupController {
    private final UserService userService;

    @PostMapping("/students")
    public ResponseEntity<BaseResponse<UserResponse>> registerStudent(@RequestBody @Valid StudentRequestDto dto) {
        BaseResponse<UserResponse> response = userService.registerUser(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/parents")
    public ResponseEntity<BaseResponse<UserResponse>> registerParent(@RequestBody @Valid ParentRequestDto dto) {
        BaseResponse<UserResponse> response = userService.registerUser(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin")
    public ResponseEntity<BaseResponse<UserResponse>> registerAdmin(@RequestBody @Valid AdminRequestDto dto) {
        BaseResponse<UserResponse> response = userService.registerUser(dto);
        return ResponseEntity.ok(response);
    }

}
