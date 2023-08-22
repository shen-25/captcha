package com.zs.captcha.handler;

import cloud.tianai.captcha.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author 35536
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<String> handleNullPointerException(IllegalArgumentException e) {
        return ApiResponse.ofError(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<String> handler(RuntimeException e) {
        return ApiResponse.ofError(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<String> handleException(Exception e) {
        return ApiResponse.ofError(e.getMessage());
    }
}
