package com.querisek.expensetracker.domain.values;

import java.util.Set;

public class Category {
    private final String value;
    private static final Set<String> VALID_CATEGORIES = Set.of("Jedzenie", "Podróże", "Zdrowie", "Rozrywka", "Dom", "Inne");

    public Category(String value) {
        validate(value);
        this.value = value;
    }

    private void validate(String value) {
        if(value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Kategoria nie może być pusta.");
        }
        if(!VALID_CATEGORIES.contains(value)) {
            throw new IllegalArgumentException("Niepoprawna kategoria.");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Category that = (Category) o;
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
