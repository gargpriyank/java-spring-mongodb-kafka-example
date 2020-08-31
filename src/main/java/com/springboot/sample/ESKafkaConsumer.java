package com.springboot.sample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ESKafkaConsumer {

    @Autowired
    private MongoDBDao mongoDBDao;

    @Autowired
    private ESKafkaUtil esKafkaUtil;

    @KafkaListener(topics = "${ES_KAFKA_TOPIC_NAME}")
    public void readAndSaveMessage(String message) {
        log.info("Received message: {}", message);
        Employee employee = (Employee) esKafkaUtil.convertToPOJO(message, Employee.class);
        mongoDBDao.save(Mono.just(employee));
    }
}
