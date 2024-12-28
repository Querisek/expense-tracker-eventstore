package com.querisek.expensetracker.domain.common;

public final class Description {
    private final String value;
    private static final int MAX_LENGTH = 50;

    public Description(String value) {
        validate(value);
        this.value = value;
    }

    public static Validation validate(String value) {
        if(value == null || value.trim().isEmpty()) {
            return Validation.error("Opis nie może być pusty.");
        }
        if(value.length() > MAX_LENGTH) {
            return Validation.error("Opis nie może być dłuższy niż " + MAX_LENGTH + " znaków.");
        }
        return Validation.success();
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Description that = (Description) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}
