package com.springboot.sample;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableReactiveMongoRepositories
@Configuration
public class ReactiveMongoDBConfig extends AbstractReactiveMongoConfiguration {

    @Value("${MONGO_DB_URL}")
    private String dbURL;

    @Value("${MONGO_DB_NAME}")
    private String dbName;

    @Override
    public MongoClient reactiveMongoClient() {
        return MongoClients.create(dbURL);
    }

    @Override
    protected String getDatabaseName() {
        return dbName;
    }

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate() {
        return new ReactiveMongoTemplate(reactiveMongoClient(), dbName);
    }
}
