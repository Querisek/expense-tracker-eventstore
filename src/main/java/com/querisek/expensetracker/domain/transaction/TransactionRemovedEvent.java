package com.querisek.expensetracker.domain.transaction;

import com.querisek.expensetracker.domain.common.Date;

import java.time.LocalDate;
import java.util.UUID;

public class TransactionRemovedEvent {
    private UUID transactionId;
    private String userEmail;
    private Date removedAt;

    public TransactionRemovedEvent() {}

    public TransactionRemovedEvent(UUID transactionId, String userEmail, LocalDate removedAt) {
        this.transactionId = transactionId;
        this.userEmail = userEmail;
        this.removedAt = new Date(removedAt);
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public LocalDate getRemovedAt() {
        return removedAt.getValue();
    }

    public void setRemovedAt(LocalDate removedAt) {
        this.removedAt = new Date(removedAt);
    }
}
