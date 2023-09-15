package com.t1consulting.testcase.characterrepetition.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(value
            = { IllegalFormatException.class, IllegalFormatRequestBodyException.class })
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ExceptionEntity handleIllegalFormats(RuntimeException e) {
        return new ExceptionEntity(e.getMessage(), e.getLocalizedMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionEntity handleNotFound(NotFoundException e) {
        return new ExceptionEntity(e.getMessage(), e.getLocalizedMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionEntity handleReadRequestException(ReadRequestBodyException e) {
        return new ExceptionEntity(e.getMessage(), e.getLocalizedMessage());
    }
}
