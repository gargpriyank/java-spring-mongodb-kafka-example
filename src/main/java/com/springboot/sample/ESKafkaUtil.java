package com.springboot.sample;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class ESKafkaUtil {

    @Value("${ES_KAFKA_SERVICE}")
    private String esKafkaService;

    private static final ObjectMapper mapper = new ObjectMapper();

    public ESKafkaCredentials getEventStreamsCredentials() {
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

    public String convertToJSON(Object object) {
        try {
            if (object != null) {
                return mapper.writeValueAsString(object);
            } else {
                log.error("object variable is null.");
                throw new IllegalStateException("object variable is null.");
            }
        } catch (IOException ioe) {
            log.error("object variable parsing failed.");
            throw new IllegalStateException("object variable parsing failed.", ioe);
        }
    }

    public Object convertToPOJO(String payload, Class<?> clazz) {
        try {
            if (payload != null) {
                return mapper.readValue(payload, clazz);
            } else {
                log.error("payload variable is null.");
                throw new IllegalStateException("payload variable is null.");
            }
        } catch (IOException ioe) {
            log.error("payload variable parsing failed.");
            throw new IllegalStateException("payload variable parsing failed.", ioe);
        }
    }
}
