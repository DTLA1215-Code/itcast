package com.itheima.health.controller;

import com.itheima.health.entity.Result;
import com.itheima.health.exception.HealthException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HealthExceptionController {

    @ExceptionHandler(HealthException.class)
    public Result handleHealthException(HealthException he){
        return new Result(false,he.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception ex){
        return new Result(false,"发生未知错误。。。");
    }
}
