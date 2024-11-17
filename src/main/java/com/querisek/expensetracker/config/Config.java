package com.querisek.expensetracker.config;

import com.eventstore.dbclient.EventStoreDBClient;
import com.eventstore.dbclient.EventStoreDBConnectionString;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public EventStoreDBClient EventStoreDBClient() {
        return EventStoreDBClient.create(
                EventStoreDBConnectionString.parseOrThrow("esdb://localhost:2113?tls=false"));
    }
}
