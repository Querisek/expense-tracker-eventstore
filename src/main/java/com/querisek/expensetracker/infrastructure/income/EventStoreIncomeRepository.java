package com.querisek.expensetracker.infrastructure.income;

import com.eventstore.dbclient.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querisek.expensetracker.domain.expense.Expense;
import com.querisek.expensetracker.domain.expense.ExpenseCreatedEvent;
import com.querisek.expensetracker.domain.expense.ExpenseDeletedEvent;
import com.querisek.expensetracker.domain.income.Income;
import com.querisek.expensetracker.domain.income.IncomeCreatedEvent;
import com.querisek.expensetracker.domain.income.IncomeDeletedEvent;
import com.querisek.expensetracker.domain.income.IncomeRepository;
import com.querisek.expensetracker.infrastructure.expense.EventStoreExpenseRepository;
import org.springframework.stereotype.Repository;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class EventStoreIncomeRepository implements IncomeRepository {
    private final EventStoreDBClient eventStoreDBClient;
    private final ObjectMapper objectMapper;

    public EventStoreIncomeRepository(EventStoreDBClient eventStoreDBClient, ObjectMapper objectMapper) {
        this.eventStoreDBClient = eventStoreDBClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public void addIncome(Income income) {
        try {
            IncomeCreatedEvent event = new IncomeCreatedEvent(income);
            String streamName = String.format("%s-%s", income.getUserId(), "incomes");
            byte[] eventBytes = objectMapper.writeValueAsBytes(event);
            EventData eventData = EventData.builderAsJson("IncomeCreated", eventBytes).build();
            eventStoreDBClient.appendToStream(streamName, eventData).get();
        } catch (Exception e) {
            throw new RuntimeException("Nie udalo sie dodac przychodu.", e);
        }
    }

    @Override
    public void deleteIncome(Income income) {
        try {
            IncomeDeletedEvent event = new IncomeDeletedEvent(income);
            String streamName = String.format("%s-%s", income.getUserId(), "incomes");
            byte[] eventBytes = objectMapper.writeValueAsBytes(event);
            EventData eventData = EventData.builderAsJson("IncomeDeleted", eventBytes).build();
            eventStoreDBClient.appendToStream(streamName, eventData).get();
        } catch (Exception e) {
            throw new RuntimeException("Nie udalo sie usunac przychodu.", e);
        }
    }

    @Override
    public List<IncomeCreatedEvent> listUsersIncomes(String userId) {
        try {
            String streamName = String.format("%s-%s", userId, "incomes");
            ReadStreamOptions options = ReadStreamOptions.get()
                    .forwards()
                    .fromStart();
            List<IncomeCreatedEvent> usersIncomes = new ArrayList<>();
            try {
                ReadResult result = eventStoreDBClient.readStream(streamName, options).get();
                for (ResolvedEvent event : result.getEvents()) {
                    String eventBody = new String(event.getEvent().getEventData(), StandardCharsets.UTF_8);
                    String eventType = event.getEvent().getEventType();
                    if(eventType.equals("IncomeCreated")) {
                        IncomeCreatedEvent income = objectMapper.readValue(eventBody, IncomeCreatedEvent.class);
                        usersIncomes.add(income);
                    } else if(eventType.equals("IncomeDeleted")) {
                        IncomeDeletedEvent deletedIncome = objectMapper.readValue(eventBody, IncomeDeletedEvent.class);
                        usersIncomes.removeIf(expense -> expense.getIncomeId().equals(deletedIncome.getIncomeId()));
                    }
                }
            } catch (ExecutionException e) {
                if(e.getCause() instanceof StreamNotFoundException) {
                    return usersIncomes;
                }
            }
            return usersIncomes;
        } catch(Exception e) {
            throw new RuntimeException("Nie udalo sie odczytac wszystkich eventow uzytkownika.", e);
        }
    }
}
