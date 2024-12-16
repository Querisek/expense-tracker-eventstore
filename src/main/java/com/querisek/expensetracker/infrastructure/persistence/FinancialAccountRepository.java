package com.querisek.expensetracker.infrastructure.persistence;

import com.eventstore.dbclient.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querisek.expensetracker.domain.*;
import com.querisek.expensetracker.domain.expense.Expense;
import com.querisek.expensetracker.domain.income.Income;
import com.querisek.expensetracker.domain.transaction.Transaction;
import com.querisek.expensetracker.domain.transaction.TransactionAddedEvent;
import com.querisek.expensetracker.domain.transaction.TransactionRemovedEvent;
import org.springframework.stereotype.Repository;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Repository
public class FinancialAccountRepository {
    private final EventStoreDBClient eventStoreDBClient;
    private final ObjectMapper objectMapper;

    public FinancialAccountRepository(EventStoreDBClient eventStoreDBClient, ObjectMapper objectMapper) {
        this.eventStoreDBClient = eventStoreDBClient;
        this.objectMapper = objectMapper;
    }

    public void addTransaction(FinancialAccount financialAccount, Transaction transaction) {
        try {
            TransactionAddedEvent event = createTransactionAddedEvent(financialAccount.getUserId(), transaction);
            String streamName = String.format("FinancialAccount-%s", financialAccount.getUserId());
            byte[] eventBytes = objectMapper.writeValueAsBytes(event);
            EventData eventData = EventData.builderAsJson("TransactionAdded", eventBytes).build();
            eventStoreDBClient.appendToStream(streamName, eventData).get();
        } catch (Exception e) {
            throw new RuntimeException("Nie udalo sie dodac transakcji.", e);
        }
    }

    public void removeTransaction(FinancialAccount financialAccount, UUID transactionId) {
        try {
            TransactionRemovedEvent event = new TransactionRemovedEvent(transactionId, financialAccount.getUserId(), LocalDate.now());
            String streamName = String.format("FinancialAccount-%s", financialAccount.getUserId());
            byte[] eventBytes = objectMapper.writeValueAsBytes(event);
            EventData eventData = EventData.builderAsJson("TransactionRemoved", eventBytes).build();
            eventStoreDBClient.appendToStream(streamName, eventData).get();
        } catch (Exception e) {
            throw new RuntimeException("Nie udalo sie usunac transakcji.", e);
        }
    }

    public FinancialAccount buildFinancialAccount(String userId) {
        try {
            List<Transaction> transactions = new ArrayList<>();
            String streamName = String.format("FinancialAccount-%s", userId);
            ReadStreamOptions options = ReadStreamOptions.get()
                    .forwards()
                    .fromStart();
            try {
                ReadResult result = eventStoreDBClient.readStream(streamName, options).get();
                for (ResolvedEvent event : result.getEvents()) {
                    String eventBody = new String(event.getEvent().getEventData(), StandardCharsets.UTF_8);
                    String eventType = event.getEvent().getEventType();
                    if (eventType.equals("TransactionAdded")) {
                        TransactionAddedEvent transactionEvent = objectMapper.readValue(eventBody, TransactionAddedEvent.class);
                        Transaction transaction = createTransactionFromEvent(transactionEvent);
                        transactions.add(transaction);
                    } else if (eventType.equals("TransactionRemoved")) {
                        TransactionRemovedEvent removedEvent = objectMapper.readValue(eventBody, TransactionRemovedEvent.class);
                        transactions.removeIf(transaction -> transaction.getId().equals(removedEvent.getTransactionId()));
                    }
                }
            } catch (ExecutionException e) {
                if (e.getCause() instanceof StreamNotFoundException) {
                    return new FinancialAccount(userId, transactions);
                }
            }
            return new FinancialAccount(userId, transactions);
        } catch (Exception e) {
            throw new RuntimeException("Nie udalo sie przeczytac transakcji.", e);
        }
    }

    private TransactionAddedEvent createTransactionAddedEvent(String userId, Transaction transaction) {
        TransactionAddedEvent event = new TransactionAddedEvent();
        event.setTransactionId(transaction.getId());
        event.setUserId(userId);
        if(transaction instanceof Expense) {
            event.setType("EXPENSE");
            event.setCategory(((Expense) transaction).getCategory());
        } else if(transaction instanceof Income) {
            event.setType("INCOME");
            event.setCategory(null);
        }
        event.setDescription(transaction.getDescription());
        event.setPrice(transaction.getPrice());
        event.setCreatedAt(transaction.getCreatedAt());

        return event;
    }

    private Transaction createTransactionFromEvent(TransactionAddedEvent event) {
        if(event.getType().equals("EXPENSE")) {
            return new Expense(
                    event.getTransactionId(),
                    event.getCategory(),
                    event.getDescription(),
                    event.getPrice(),
                    event.getCreatedAt()
            );
        } else {
            return new Income(
                    event.getTransactionId(),
                    event.getDescription(),
                    event.getPrice(),
                    event.getCreatedAt()
            );
        }
    }
}
