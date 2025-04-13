package com.tredbase.payment.exception;

import com.tredbase.payment.dto.response.BaseResponse;
import com.tredbase.payment.dto.response.ResponseCodes;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalValidExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {

        BaseResponse baseResponse = new BaseResponse();

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        String errorMessage = errors.toString().replace("{", "").replace("}", "").replace("=", ":");

        baseResponse.setCode(ResponseCodes.ERROR);
        baseResponse.setDescription(errorMessage);

        return new ResponseEntity<>(baseResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Object>> handleGenericException(Exception ex) {
        BaseResponse<Object> response = new BaseResponse<>(ResponseCodes.ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ParentNotFoundException.class)
    public ResponseEntity<BaseResponse<Object>> handleParentNotFoundException(ParentNotFoundException ex) {
        BaseResponse<Object> response = new BaseResponse<>(ResponseCodes.ERROR,  ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<BaseResponse<Object>> handleStudentNotFoundException(StudentNotFoundException ex) {
        BaseResponse<Object> response = new BaseResponse<>(ResponseCodes.ERROR,  ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<BaseResponse<Object>> handleInsufficientBalanceException(InsufficientBalanceException ex) {
        BaseResponse<Object> response = new BaseResponse<>(ResponseCodes.ERROR,  ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<BaseResponse<Object>> handleIllegalStateException(IllegalStateException ex) {
        BaseResponse<Object> response = new BaseResponse<>(ResponseCodes.ERROR,  ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPaymentTypeException.class)
    public ResponseEntity<BaseResponse<Object>> handleInvalidPaymentTypeException(InvalidPaymentTypeException ex) {
        BaseResponse<Object> response = new BaseResponse<>(ResponseCodes.ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<BaseResponse<Object>> handleSignatureException(SignatureException ex) {
        BaseResponse<Object> response = new BaseResponse<>(ResponseCodes.ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }


}
