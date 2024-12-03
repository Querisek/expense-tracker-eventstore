package com.querisek.expensetracker.infrastructure.expense;

import com.eventstore.dbclient.AppendToStreamOptions;
import com.eventstore.dbclient.EventData;
import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.ExpectedRevision;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querisek.expensetracker.domain.Expense;
import com.querisek.expensetracker.domain.ExpenseCreatedEvent;
import com.querisek.expensetracker.domain.ExpenseRepository;
import org.springframework.stereotype.Repository;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Repository
public class EventStoreExpenseRepository implements ExpenseRepository {
    private final EventStoreDBClient eventStoreDBClient;
    private final ObjectMapper objectMapper;

    public EventStoreExpenseRepository(EventStoreDBClient eventStoreDBClient, ObjectMapper objectMapper) {
        this.eventStoreDBClient = eventStoreDBClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public void addExpense(Expense expense) {
        try {
            ExpenseCreatedEvent event = new ExpenseCreatedEvent(expense);
            String streamName = String.format("%s-%s", expense.getUserId(), expense.getExpenseCategory());
            byte[] eventBytes = objectMapper.writeValueAsBytes(event);
            EventData eventData = EventData.builderAsJson("ExpenseCreated", eventBytes).build();
            eventStoreDBClient.appendToStream(streamName, eventData).get();
        } catch (Exception e) {
            throw new RuntimeException("Nie udalo sie dodac wydatku.", e);
        }
    }
}
