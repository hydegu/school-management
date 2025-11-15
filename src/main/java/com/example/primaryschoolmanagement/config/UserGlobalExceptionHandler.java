package com.example.primaryschoolmanagement.config;


import com.example.primaryschoolmanagement.common.exception.GlobalExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserGlobalExceptionHandler extends GlobalExceptionHandler {
    // 继承公共异常处理器
}
