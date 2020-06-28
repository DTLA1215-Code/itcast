package com.itheima.health.controller;

import com.itheima.health.entity.Result;
import com.itheima.health.exception.HealthException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HealExceptionAdvice {

    @ExceptionHandler(HealthException.class)
    public Result HandlerHealthException(HealthException he){
        return new Result(false, he.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result HandlerException(Exception ex){
        return new Result(false, "error,please find the engineer and fix the bug");
    }
}
