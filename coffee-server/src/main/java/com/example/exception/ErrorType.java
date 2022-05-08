package com.example.exception;

public enum ErrorType {
    DATA_NOT_INSERTED("Data is not inserted."),
    DATA_NOT_UPDATED("Data is not updated.");

    private final String message;

    ErrorType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
