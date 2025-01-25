package com.querisek.expensetracker.domain.common;

import com.google.common.collect.ImmutableSet;

public final class Category {
    private final String value;
    private static final ImmutableSet<String> VALID_CATEGORIES = ImmutableSet.of("Jedzenie", "Podróże", "Zdrowie", "Rozrywka", "Dom", "Inne");

    public Category(String value) {
        this.value = value;
    }

    public static Validation validate(String value) {
        if(value == null || value.trim().isEmpty()) {
            return Validation.error("Kategoria nie może być pusta.");
        }
        if(!VALID_CATEGORIES.contains(value)) {
            return Validation.error("Niepoprawna kategoria.");
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
