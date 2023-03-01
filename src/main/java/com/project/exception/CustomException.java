package com.project.exception;

//@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CustomException extends RuntimeException{
    public CustomException() {
        super();
    }

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomException(Throwable cause) {
        super(cause);
    }
}
