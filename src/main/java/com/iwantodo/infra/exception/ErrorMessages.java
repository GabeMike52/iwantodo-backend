package com.iwantodo.infra.exception;

public enum ErrorMessages {
    NAME_REQUIRED("Username is required"),
    EMAIL_NOT_VALID("Email using invalid format"),
    EMAIL_REQUIRED("Email is required"),
    PASSWORD_REQUIRED("Password is required"),
    PASSWORD_LENGTH("Password must be at least 8 chars"),
    USER_ALREADY_EXISTS("A user with this username already exists");

    private final String message;
    ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
