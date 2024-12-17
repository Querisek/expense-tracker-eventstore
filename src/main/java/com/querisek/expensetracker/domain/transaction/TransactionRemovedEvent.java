package com.querisek.expensetracker.domain.transaction;

import com.querisek.expensetracker.domain.common.Date;

import java.time.LocalDate;
import java.util.UUID;

public class TransactionRemovedEvent {
    private UUID transactionId;
    private String userId;
    private Date removedAt;

    public TransactionRemovedEvent() {}

    public TransactionRemovedEvent(UUID transactionId, String userId, LocalDate removedAt) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.removedAt = new Date(removedAt);
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDate getRemovedAt() {
        return removedAt.getValue();
    }

    public void setRemovedAt(LocalDate removedAt) {
        this.removedAt = new Date(removedAt);
    }
}
