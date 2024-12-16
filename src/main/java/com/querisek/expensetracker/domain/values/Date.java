package com.querisek.expensetracker.domain.values;

import java.time.LocalDate;

public class Date {
    private final LocalDate value;

    public Date(LocalDate value) {
        validate(value);
        this.value = value;
    }

    private void validate(LocalDate value) {
        if(value == null) {
            throw new IllegalArgumentException("Brak daty.");
        }
        if(value.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Nie można wybrać daty, która jeszcze się nie wydarzyła.");
        }
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
