package com.querisek.expensetracker.infrastructure.persistence;

import com.eventstore.dbclient.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querisek.expensetracker.domain.FinancialAccount;
import com.querisek.expensetracker.domain.snapshot.MonthlySnapshot;
import com.querisek.expensetracker.domain.transaction.TransactionAddedEvent;
import com.querisek.expensetracker.domain.transaction.TransactionRemovedEvent;
import org.springframework.stereotype.Repository;

import java.nio.charset.StandardCharsets;
import java.time.YearMonth;
import java.util.concurrent.ExecutionException;

@Repository
public class FinancialAccountRepository {
    private final EventStoreDBClient eventStoreDBClient;
    private final ObjectMapper objectMapper;

    public FinancialAccountRepository(EventStoreDBClient eventStoreDBClient, ObjectMapper objectMapper) {
        this.eventStoreDBClient = eventStoreDBClient;
        this.objectMapper = objectMapper;
    }

    public void save(FinancialAccount financialAccount, YearMonth currentMonth) {
        try {
            String streamName = String.format("FinancialAccount-%s-%s", financialAccount.getUserEmail(), currentMonth);
            Object event = financialAccount.getUncommitedEvent();
            if(event != null) {
                if(isFirstEventInMonth(streamName)) {
                    YearMonth previousMonth = currentMonth.minusMonths(1);
                    String previousStreamName = String.format("FinancialAccount-%s-%s", financialAccount.getUserEmail(), previousMonth);
                    FinancialAccount accountFromPreviousMonth = buildFinancialAccount(financialAccount.getUserEmail(), previousMonth);
                    MonthlySnapshot snapshot = MonthlySnapshot.createFromAccount(accountFromPreviousMonth, previousMonth);
                    EventData snapshotEvent = EventData.builderAsJson("PreviousMonthSummaryEvent", objectMapper.writeValueAsBytes(snapshot)).build();
                    EventData monthClosedEvent = EventData.builderAsJson("MonthClosedEvent", objectMapper.writeValueAsBytes(snapshot)).build();
                    eventStoreDBClient.appendToStream(streamName, snapshotEvent).get();
                    eventStoreDBClient.appendToStream(previousStreamName, monthClosedEvent).get();
                }
                String eventType = event.getClass().getSimpleName();
                byte[] data = objectMapper.writeValueAsBytes(event);
                EventData eventData = EventData.builderAsJson(eventType, data).build();
                eventStoreDBClient.appendToStream(streamName, eventData).get();
            }
        } catch (Exception e) {
            throw new RuntimeException("Wystapil problem przy dodawaniu transakcji.", e);
        }
    }

    public FinancialAccount buildFinancialAccount(String userEmail, YearMonth yearMonth) {
        try {
            String streamName = String.format("FinancialAccount-%s-%s", userEmail, yearMonth);
            FinancialAccount financialAccount = new FinancialAccount(userEmail);
            ReadStreamOptions options = ReadStreamOptions.get()
                    .forwards()
                    .fromStart()
                    .maxCount(1);
            try {
                ReadResult result = eventStoreDBClient.readStream(streamName, options).get();
                if(!result.getEvents().isEmpty()) {
                    ResolvedEvent firstEvent = result.getEvents().get(0);
                    if(firstEvent.getEvent().getEventType().equals("PreviousMonthSummaryEvent")) {
                        MonthlySnapshot snapshot = objectMapper.readValue(firstEvent.getEvent().getEventData(), MonthlySnapshot.class);
                        financialAccount.loadFromSnapshot(
                                snapshot.getTotalExpenses(),
                                snapshot.getTotalIncome(),
                                snapshot.getExpensesByCategory()
                        );
                    }
                }
                options = ReadStreamOptions.get()
                        .forwards()
                        .fromStart();
                result = eventStoreDBClient.readStream(streamName, options).get();
                for(ResolvedEvent resolvedEvent : result.getEvents()) {
                    if (!resolvedEvent.getEvent().getEventType().equals("PreviousMonthSummaryEvent")) {
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
                    }
                }
            } catch(ExecutionException e) {
                if(!(e.getCause() instanceof StreamNotFoundException)) {
                    throw new RuntimeException("Wystapil problem podczas odczytywania historii transakcji konta.", e);
                }
            }
            return financialAccount;
        } catch(Exception e) {
            throw new RuntimeException("Wystapil problem podczas odczytywania historii transakcji konta.", e);
        }
    }

    private boolean isFirstEventInMonth(String streamName) {
        try {
            ReadStreamOptions options = ReadStreamOptions.get()
                    .maxCount(1)
                    .fromStart();
            ReadResult result = eventStoreDBClient.readStream(streamName, options).get();
            return result.getEvents().isEmpty();
        } catch(Exception e) {
            if(e.getCause() instanceof StreamNotFoundException) {
                return true;
            }
            throw new RuntimeException("Wystapil problem podczas sprawdzania strumienia.", e);
        }
    }
}
