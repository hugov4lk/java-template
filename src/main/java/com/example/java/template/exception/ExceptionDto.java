package com.example.java.template.exception;

public record ExceptionDto(String message, String exception, ServiceErrorCode errorCode) {
}
