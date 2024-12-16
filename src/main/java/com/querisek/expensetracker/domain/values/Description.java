package com.querisek.expensetracker.domain.values;

public class Description {
    private final String value;
    private static final int MAX_LENGTH = 10;

    public Description(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Opis nie może być pusty.");
        }
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Opis nie może być dłuższy niż " + MAX_LENGTH + " znaków.");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
