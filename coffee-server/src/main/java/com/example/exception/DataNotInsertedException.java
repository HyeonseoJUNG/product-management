package com.example.exception;

public class DataNotInsertedException extends RuntimeException {
    public DataNotInsertedException() {
        super(ErrorType.DATA_NOT_INSERTED.getMessage());
    }

    public DataNotInsertedException(String message) {
        super(message);
    }

    public DataNotInsertedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataNotInsertedException(Throwable cause) {
        super(cause);
    }
}
