package com.springboot.sample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Slf4j
@Service
public class AppService {

    @Autowired
    private ESKafkaProducer esKafkaProducer;

    @Autowired
    private MongoDBDao mongoDBDao;

    public void sendMessageToESKafka(String message) {
        esKafkaProducer.sendMessage(UUID.randomUUID().toString(), message);
    }

    public Flux<Employee> getAllEmployees() {
        return mongoDBDao.findAll(Employee.class).cast(Employee.class);
    }
}
