package com.querisek.expensetracker.domain.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Money {
    private final BigDecimal value;
    private static final int DECIMAL_PLACES = 2;
    private static final BigDecimal MIN_VALUE = BigDecimal.ZERO;
    private static final BigDecimal MAX_VALUE = new BigDecimal("1000000000.00");

    public Money(double value) {
        this(BigDecimal.valueOf(value));
    }

    public Money(BigDecimal value) {
        this.value = value.setScale(DECIMAL_PLACES, RoundingMode.HALF_UP);
    }

    public static Validation validate(BigDecimal value) {
        if(value == null) {
            return Validation.error("Nie podano kwoty.");
        }
        BigDecimal roundedValue = value.setScale(DECIMAL_PLACES, RoundingMode.HALF_UP);
        if(roundedValue.compareTo(MIN_VALUE) < 0) {
            return Validation.error("Kwota musi być większa od zera.");
        }
        if(roundedValue.compareTo(MAX_VALUE) > 0) {
            return Validation.error("Kwota nie może być większa niż " + MAX_VALUE + " PLN.");
        }
        return Validation.success();
    }

    public double getValue() {
        return value.doubleValue();
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return value.compareTo(money.value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString() + " PLN";
    }
}
