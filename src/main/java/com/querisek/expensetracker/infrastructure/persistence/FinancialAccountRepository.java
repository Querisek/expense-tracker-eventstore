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
import java.util.stream.Collectors;

@Repository
public class FinancialAccountRepository {
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
            FinancialAccount financialAccount = new FinancialAccount(userId);
            ReadStreamOptions options = ReadStreamOptions.get()
                    .forwards()
                    .fromStart();
            try {
                ReadResult result = eventStoreDBClient.readStream(streamName, options).get();
                for(ResolvedEvent resolvedEvent : result.getEvents()) {
                    String eventBody = new String(resolvedEvent.getEvent().getEventData(), StandardCharsets.UTF_8);
                    String eventType = resolvedEvent.getEvent().getEventType();
                    switch(eventType) {
                        case "TransactionAddedEvent" -> {
                            TransactionAddedEvent event = objectMapper.readValue(eventBody, TransactionAddedEvent.class);
                            if(event.getType().equals("EXPENSE")) {
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
                }
            } catch(ExecutionException e) {
                if (!(e.getCause() instanceof StreamNotFoundException)) {
                    throw new ExecutionException(e);
                }
            }
            return financialAccount;
        } catch(Exception e) {
            throw new RuntimeException("Nie udalo sie wczytac historii transakcji konta.", e);
        }
    }
}
