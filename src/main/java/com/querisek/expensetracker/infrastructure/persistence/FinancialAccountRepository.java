package com.querisek.expensetracker.infrastructure.persistence;

import com.eventstore.dbclient.*;
import com.fasterxml.jackson.databind.JsonNode;
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
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Repository
public class FinancialAccountRepository {
    private static final int FREQUENCY_OF_SNAPSHOTS = 5;
    private final EventStoreDBClient eventStoreDBClient;
    private final ObjectMapper objectMapper;

    public FinancialAccountRepository(EventStoreDBClient eventStoreDBClient, ObjectMapper objectMapper) {
        this.eventStoreDBClient = eventStoreDBClient;
        this.objectMapper = objectMapper;
    }

    public void save(FinancialAccount financialAccount) {
        try {
            String streamName = String.format("FinancialAccount-%s", financialAccount.getUserId());
            Object event = financialAccount.getUncommitedEvent();
            if(event != null) {
                String eventType = event.getClass().getSimpleName();
                byte[] data = objectMapper.writeValueAsBytes(event);
                EventData eventData = EventData.builderAsJson(eventType, data).build();
                eventStoreDBClient.appendToStream(streamName, eventData).get();
            }
        } catch (Exception e) {
            throw new RuntimeException("Nie udalo sie dodac transakcji.", e);
        }
    }

    public FinancialAccount buildFinancialAccount(String userId) {
        try {
            String streamName = String.format("FinancialAccount-%s", userId);
            FinancialAccountSnapshot snapshot = loadSnapshot(userId);
            FinancialAccount financialAccount;
            long version = 0;
            if(snapshot != null) {
                financialAccount = new FinancialAccount(snapshot);
                version = snapshot.getVersion();
            } else {
                financialAccount = new FinancialAccount(userId);
            }
            System.out.println(financialAccount.getTransactions());
            ReadStreamOptions options = ReadStreamOptions.get()
                    .forwards()
                    .fromRevision(version);
            try {
                ReadResult result = eventStoreDBClient.readStream(streamName, options).get();
                for (ResolvedEvent resolvedEvent : result.getEvents()) {
                    String eventBody = new String(resolvedEvent.getEvent().getEventData(), StandardCharsets.UTF_8);
                    String eventType = resolvedEvent.getEvent().getEventType();
                    switch (eventType) {
                        case "TransactionAddedEvent" -> {
                            TransactionAddedEvent event = objectMapper.readValue(eventBody, TransactionAddedEvent.class);
                            if (event.getType().equals("EXPENSE")) {
                                financialAccount.addExpenseFromEvent(
                                        event.getTransactionId(),
                                        event.getCategory(),
                                        event.getDescription(),
                                        event.getPrice(),
                                        event.getCreatedAt()
                                );
                            } else {
                                financialAccount.addIncomeFromEvent(
                                        event.getTransactionId(),
                                        event.getDescription(),
                                        event.getPrice(),
                                        event.getCreatedAt()
                                );
                            }
                        }
                        case "TransactionRemovedEvent" -> {
                            TransactionRemovedEvent event = objectMapper.readValue(eventBody, TransactionRemovedEvent.class);
                            financialAccount.removeTransaction(event.getTransactionId());
                        }
                    }
                    financialAccount.nextVersion();
                }
                if (financialAccount.getVersion() - version >= FREQUENCY_OF_SNAPSHOTS) {
                    saveSnapshot(financialAccount);
                }
            } catch(ExecutionException e) {
                if(!(e.getCause() instanceof StreamNotFoundException)) {
                    throw new ExecutionException(e);
                }
            }
            return financialAccount;
        } catch(Exception e) {
            throw new RuntimeException("Nie udalo sie wczytac historii transakcji konta.", e);
        }
    }

    private void saveSnapshot(FinancialAccount financialAccount) {
        try {
            String streamName = String.format("FinancialAccount-%s-snapshot", financialAccount.getUserId());
            List<TransactionAddedEvent> transactionEvents = new ArrayList<>();
            for(Transaction transaction : financialAccount.getTransactions()) {
                if(transaction instanceof Expense) {
                    transactionEvents.add(new TransactionAddedEvent(
                            transaction.getId(),
                            financialAccount.getUserId(),
                            "EXPENSE",
                            ((Expense) transaction).getCategory(),
                            transaction.getDescription(),
                            transaction.getPrice(),
                            transaction.getCreatedAt()
                    ));
                } else if(transaction instanceof Income) {
                    transactionEvents.add(new TransactionAddedEvent(
                            transaction.getId(),
                            financialAccount.getUserId(),
                            "INCOME",
                            null,
                            transaction.getDescription(),
                            transaction.getPrice(),
                            transaction.getCreatedAt()
                    ));
                }
            }
            String eventType = "FinancialAccountSnapshotAdded";
            byte[] data = objectMapper.writeValueAsBytes(transactionEvents.toArray(new TransactionAddedEvent[0]));
            EventData eventData = EventData.builderAsJson(eventType, data).build();
            eventStoreDBClient.appendToStream(streamName, eventData).get();
        } catch(Exception e) {
            throw new RuntimeException("Nie udalo sie dodac snapshota.", e);
        }
    }

    private FinancialAccountSnapshot loadSnapshot(String userId) {
        try {
            String streamName = String.format("FinancialAccount-%s-snapshot", userId);
            ReadStreamOptions options = ReadStreamOptions.get()
                    .fromEnd()
                    .backwards()
                    .maxCount(1);
            try {
                ReadResult result = eventStoreDBClient.readStream(streamName, options).get();
                if(result.getEvents().isEmpty()) {
                    return null;
                }
                ResolvedEvent resolvedEvent = result.getEvents().get(0);
                TransactionAddedEvent[] transactionEvents = objectMapper.readValue(
                        new String(resolvedEvent.getEvent().getEventData(), StandardCharsets.UTF_8),
                        TransactionAddedEvent[].class);
                List<Transaction> transactions = new ArrayList<>();
                for(TransactionAddedEvent transactionAddedEvent : transactionEvents) {
                    if(transactionAddedEvent.getType().equals("EXPENSE")) {
                        transactions.add(new Expense(
                                transactionAddedEvent.getTransactionId(),
                                transactionAddedEvent.getCategory(),
                                transactionAddedEvent.getDescription(),
                                transactionAddedEvent.getPrice(),
                                transactionAddedEvent.getCreatedAt()
                        ));
                    } else if(transactionAddedEvent.getType().equals("INCOME")) {
                        transactions.add(new Income(
                                transactionAddedEvent.getTransactionId(),
                                transactionAddedEvent.getDescription(),
                                transactionAddedEvent.getPrice(),
                                transactionAddedEvent.getCreatedAt()
                        ));
                    }
                }
                return new FinancialAccountSnapshot(userId, transactions, result.getEvents().get(0).getEvent().getRevision());
            } catch(ExecutionException e) {
                if (e.getCause() instanceof StreamNotFoundException) {
                    return null;
                }
                throw e;
            }
            } catch(Exception e) {
                throw new RuntimeException("Nie udalo sie wczytac ostatniego snapshota", e);
            }
    }
}
