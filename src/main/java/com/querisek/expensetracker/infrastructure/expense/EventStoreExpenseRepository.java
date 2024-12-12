package com.querisek.expensetracker.infrastructure.expense;

import com.eventstore.dbclient.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querisek.expensetracker.domain.expense.Expense;
import com.querisek.expensetracker.domain.expense.ExpenseCreatedEvent;
import com.querisek.expensetracker.domain.expense.ExpenseDeletedEvent;
import com.querisek.expensetracker.domain.expense.ExpenseRepository;
import org.springframework.stereotype.Repository;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

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
            String allCategoriesStreamName = String.format("%s-allCategories", expense.getUserId());
            byte[] eventBytes = objectMapper.writeValueAsBytes(event);
            EventData eventData = EventData.builderAsJson("ExpenseCreated", eventBytes).build();
            eventStoreDBClient.appendToStream(streamName, eventData).get();
            eventStoreDBClient.appendToStream(allCategoriesStreamName, eventData).get();
        } catch (Exception e) {
            throw new RuntimeException("Nie udalo sie dodac wydatku.", e);
        }
    }

    @Override
    public void deleteExpense(Expense expense) {
        try {
            ExpenseDeletedEvent event = new ExpenseDeletedEvent(expense);
            String streamName = String.format("%s-%s", expense.getUserId(), expense.getExpenseCategory());
            String allCategoriesStreamName = String.format("%s-allCategories", expense.getUserId());
            byte[] eventBytes = objectMapper.writeValueAsBytes(event);
            EventData eventData = EventData.builderAsJson("ExpenseDeleted", eventBytes).build();
            eventStoreDBClient.appendToStream(streamName, eventData).get();
            eventStoreDBClient.appendToStream(allCategoriesStreamName, eventData).get();
        } catch (Exception e) {
            throw new RuntimeException("Nie udalo sie usunac wydatku.", e);
        }
    }


    @Override
    public List<ExpenseCreatedEvent> listUsersExpensesByCategory(String userId, String categoryName) {
        try {
            String streamName = String.format("%s-%s", userId, categoryName);
            ReadStreamOptions options = ReadStreamOptions.get()
                    .forwards()
                    .fromStart();
            List<ExpenseCreatedEvent> usersExpenses = new ArrayList<>();
            try {
                ReadResult result = eventStoreDBClient.readStream(streamName, options).get();
                for (ResolvedEvent event : result.getEvents()) {
                    String eventBody = new String(event.getEvent().getEventData(), StandardCharsets.UTF_8);
                    String eventType = event.getEvent().getEventType();
                    if(eventType.equals("ExpenseCreated")) {
                        ExpenseCreatedEvent expense = objectMapper.readValue(eventBody, ExpenseCreatedEvent.class);
                        usersExpenses.add(expense);
                    } else if(eventType.equals("ExpenseDeleted")) {
                        ExpenseDeletedEvent deletedExpense = objectMapper.readValue(eventBody, ExpenseDeletedEvent.class);
                        usersExpenses.removeIf(expense -> expense.getExpenseId().equals(deletedExpense.getExpenseId()));
                    }
                }
                Collections.reverse(usersExpenses);
            } catch (ExecutionException e) {
                if(e.getCause() instanceof StreamNotFoundException) {
                    return usersExpenses;
                }
            }
            return usersExpenses;
        } catch(Exception e) {
            throw new RuntimeException("Nie udalo sie odczytac wszystkich eventow uzytkownika.", e);
        }
    }

    @Override
    public double getTotalPriceOfCategoryByDay(String category, List<ExpenseCreatedEvent> allExpensesByDay) {
        return allExpensesByDay.stream()
                .filter(expense -> expense.getExpenseCategory().equals(category))
                .mapToDouble(ExpenseCreatedEvent::getPrice)
                .sum();
    }
}
