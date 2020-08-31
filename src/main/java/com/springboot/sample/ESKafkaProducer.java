package com.springboot.sample;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ESKafkaProducer {

    @Value("${ES_KAFKA_TOPIC_NAME}")
    private String topicName;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public ESKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String key, String message) {
        log.info("Send message: {}", message);
        ProducerRecord<String, String> record = new ProducerRecord<>(topicName, key, message);
        kafkaTemplate.send(record);
    }
}
