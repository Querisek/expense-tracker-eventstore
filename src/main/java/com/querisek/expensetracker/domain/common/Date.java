package com.querisek.expensetracker.domain.common;

import java.time.LocalDate;

public final class Date {
    private final LocalDate value;

    public Date(LocalDate value) {
        validate(value);
        this.value = value;
    }

    public static Validation validate(LocalDate value) {
        if(value == null) {
            return Validation.error("Brak daty.");
        }
        if(value.isAfter(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()))) {
            return Validation.error("Nie można wybrać daty, która jeszcze się nie wydarzyła.");
        }
        return Validation.success();
    }

    public LocalDate getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Date that = (Date) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
