package com.springboot.sample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class MongoDBDao {

    @Autowired
    private ReactiveMongoTemplate template;

    public void save(Mono<Employee> employee) {
        template.save(employee).subscribe();
    }

    public Flux<?> findAll(Class<?> clazz) {
        return template.findAll(clazz);
    }
}
