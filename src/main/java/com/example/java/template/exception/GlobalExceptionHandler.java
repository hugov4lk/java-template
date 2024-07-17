package com.example.java.template.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@ResponseBody
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServiceException.class)
    public ExceptionDto handleServiceException(ServiceException ex) {
        LOGGER.error("error: Bad request", ex);
        return new ExceptionDto("Bad request", ex.getMessage(), ex.getErrorCode());
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ExceptionDto handleServiceException(NotFoundException ex) {
        LOGGER.error("error: Not found", ex);
        return new ExceptionDto("Not found", ex.getMessage(), null);
    }

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ExceptionDto handleException(Exception ex) {
        LOGGER.error("error: Internal server error", ex);
        return new ExceptionDto("Internal server error", ex.getMessage(), null);
    }
}
