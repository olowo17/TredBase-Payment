package com.tredbase.payment.service;

import com.tredbase.payment.dto.request.BaseUserRequestDto;
import com.tredbase.payment.dto.response.BaseResponse;
import com.tredbase.payment.dto.response.UserResponse;

public interface UserService {
    BaseResponse<UserResponse> registerUser (BaseUserRequestDto baseUserRequestDto);
}
