package com.querisek.expensetracker.domain.common;

public final class Description {
    private final String description;
    private static final int DESCRIPTION_MAX_LENGTH = 50;

    public Description(String description) {
        this.description = description;
    }

    public static Validation validate(String description) {
        if(description == null || description.trim().isEmpty()) {
            return Validation.error("Opis nie może być pusty.");
        }
        if(description.length() > DESCRIPTION_MAX_LENGTH) {
            return Validation.error("Opis nie może być dłuższy niż " + DESCRIPTION_MAX_LENGTH + " znaków.");
        }
        return Validation.success();
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Description that = (Description) o;
        return description.equals(that.description);
    }

    @Override
    public int hashCode() {
        return description.hashCode();
    }

    @Override
    public String toString() {
        return description;
    }
}
