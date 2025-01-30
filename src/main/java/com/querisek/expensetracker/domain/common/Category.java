package com.querisek.expensetracker.domain.common;

import com.google.common.collect.ImmutableSet;

public final class Category {
    private final String name;
    private static final ImmutableSet<String> CATEGORIES = ImmutableSet.of("food", "travel", "health", "entertainment", "home", "other");

    public Category(String name) {
        this.name = name;
    }

    public static Validation validate(String name) {
        if(name == null || name.trim().isEmpty()) {
            return Validation.error("Kategoria nie może być pusta.");
        }
        if(!CATEGORIES.contains(name)) {
            return Validation.error("Niepoprawna kategoria.");
        }
        return Validation.success();
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Category that = (Category) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
