package com.itheima.health.controller;

import com.itheima.health.entity.Result;
import com.itheima.health.exception.HealthException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//此注解通过对异常的拦截，实现统一异常返回处理
@RestControllerAdvice
public class HealExceptionAdvice {

    @ExceptionHandler(HealthException.class)
    public Result HandlerHealthException(HealthException he){
        return new Result(false, he.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result HandlerException(Exception ex){
        ex.printStackTrace();
        return new Result(false, "error,please find the engineer and fix the bug");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public Result handBadCredentialsException(BadCredentialsException he){
        return handleUserPassword();
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public Result handInternalAuthenticationServiceException(InternalAuthenticationServiceException he){
        return handleUserPassword();
    }

    private Result handleUserPassword(){
        return new Result(false,"用户名或密码不正确");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Result handAccessDeniedException(AccessDeniedException ae){
        return new Result(false,"没有权限");
    }


}
