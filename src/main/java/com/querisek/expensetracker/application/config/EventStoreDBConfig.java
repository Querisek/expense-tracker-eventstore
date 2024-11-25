package com.querisek.expensetracker.application.config;

import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.EventStoreDBConnectionString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventStoreDBConfig {
    @Bean
    public EventStoreDBClient EventStoreDBClient(@Value("${esdb.connectionstring}") String connectionString) {
        return EventStoreDBClient.create(
                EventStoreDBConnectionString.parseOrThrow(connectionString));
    }
}
