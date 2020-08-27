package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class ESKafkaUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static ESKafkaCredentials getEventStreamsCredentials() {
        String esKafkaService = System.getenv("ES_KAFKA_SERVICE");
        try {
            if (esKafkaService != null) {
                return mapper.readValue(esKafkaService, ESKafkaCredentials.class);
            } else {
                log.error("ES_KAFKA_SERVICE environment variable is null.");
                throw new IllegalStateException("ES_KAFKA_SERVICE environment variable is null.");
            }
        } catch (IOException ioe) {
            log.error("ES_KAFKA_SERVICE environment variable parsing failed.");
            throw new IllegalStateException("ES_KAFKA_SERVICE environment variable parsing failed.", ioe);
        }
    }
}
