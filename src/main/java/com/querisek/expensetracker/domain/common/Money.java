package com.querisek.expensetracker.domain.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Money {
    private final BigDecimal amount;
    private static final BigDecimal MIN_AMOUNT = BigDecimal.ZERO;
    private static final BigDecimal MAX_AMOUNT = new BigDecimal("1000000000.00");

    public Money(double amount) {
        this(BigDecimal.valueOf(amount));
    }

    public Money(BigDecimal amount) {
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
    }

    public static Validation validate(BigDecimal amount) {
        if(amount == null) {
            return Validation.error("Nie podano kwoty.");
        }
        BigDecimal roundedAmount = amount.setScale(2, RoundingMode.HALF_UP);
        if(roundedAmount.compareTo(MIN_AMOUNT) <= 0) {
            return Validation.error("Kwota musi być większa od zera.");
        }
        if(roundedAmount.compareTo(MAX_AMOUNT) > 0) {
            return Validation.error("Kwota nie może być większa niż " + MAX_AMOUNT + " PLN.");
        }
        return Validation.success();
    }

    public double getAmount() {
        return amount.doubleValue();
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount.compareTo(money.amount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    @Override
    public String toString() {
        return amount.toString() + " PLN";
    }
}
