package com.tredbase.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tredbase.payment.utils.ResponseConstants;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {
    private int code;
    private String description;
    private T data;

    public BaseResponse() {
        this(ResponseConstants.ERROR_CODE, ResponseConstants.ERROR_MESSAGE);
    }

    public BaseResponse(int code) {
        this(code, ResponseConstants.ERROR_MESSAGE);
    }

    public BaseResponse(int code, String description) {
        this(code, description, null);
    }

    public BaseResponse(int code, String description, T data) {
        this.code = code;
        this.description = description;
        this.data = data;
    }

}
