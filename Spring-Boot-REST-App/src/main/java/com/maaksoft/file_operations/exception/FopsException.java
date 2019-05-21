package com.maaksoft.file_operations.exception;

public class FopsException extends RuntimeException {

    public FopsException(String message) {
        super(message);
    }

    public FopsException(String message, Throwable cause) {
        super(message, cause);
    }

}
