package com.querisek.expensetracker.domain.common;

import java.time.LocalDate;

public final class Date {
    private final LocalDate date;

    public Date(LocalDate date) {
        this.date = date;
    }

    public static Validation validate(LocalDate date) {
        if(date == null) {
            return Validation.error("Brak daty.");
        }
        if(date.isAfter(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth())) || date.isBefore(LocalDate.now().withDayOfMonth(1))) {
            return Validation.error("Wybór daty jest dostępny tylko w zakresie aktualnego miesiąca.");
        }
        return Validation.success();
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Date that = (Date) o;
        return date.equals(that.date);
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }

    @Override
    public String toString() {
        return date.toString();
    }
}
