package com.example.exception;

public class DataNotUpdatedException extends RuntimeException{
    public DataNotUpdatedException() {
        super(ErrorType.DATA_NOT_UPDATED.getMessage());
    }

    public DataNotUpdatedException(String message) {
        super(message);
    }

    public DataNotUpdatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataNotUpdatedException(Throwable cause) {
        super(cause);
    }
}
