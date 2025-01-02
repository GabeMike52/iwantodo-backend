package com.iwantodo.infra.exception;

public enum ErrorMessages {
    NAME_REQUIRED("Username is required"),
    NAME_INVALID("Username using invalid format"),
    EMAIL_NOT_VALID("Email using invalid format"),
    EMAIL_REQUIRED("Email is required"),
    PASSWORD_REQUIRED("Password is required"),
    PASSWORD_LENGTH("Password must be at least 8 chars"),
    USER_ALREADY_EXISTS("A user with this username already exists"),
    USER_NOT_FOUND("No users found"),
    NOT_AUTHENTICATED("User not authenticated"),
    TITLE_REQUIRED("Title is required"),
    STATUS_REQUIRED("A status for the event is required"),
    EVENT_NOT_FOUND("No event found");

    private final String message;
    ErrorMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
