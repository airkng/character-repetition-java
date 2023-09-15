package com.t1consulting.testcase.characterrepetition.exceptions;

public class IllegalFormatRequestBodyException extends RuntimeException {
    public IllegalFormatRequestBodyException() {
    }

    public IllegalFormatRequestBodyException(String message) {
        super(message);
    }
}
