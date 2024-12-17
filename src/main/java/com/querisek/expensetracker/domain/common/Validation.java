package com.querisek.expensetracker.domain.common;

public class Validation {
    private final boolean valid;
    private final String message;

    public Validation(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }

    public static Validation success() {
        return new Validation(true, null);
    }

    public static Validation error(String message) {
        return new Validation(false, message);
    }

    public boolean isValid() {
        return valid;
    }

    public String getMessage() {
        return message;
    }
}
