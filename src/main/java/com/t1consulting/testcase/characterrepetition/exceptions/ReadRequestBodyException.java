package com.t1consulting.testcase.characterrepetition.exceptions;

public class ReadRequestBodyException extends RuntimeException {
    public ReadRequestBodyException() {
    }

    public ReadRequestBodyException(String message) {
        super(message);
    }
}
