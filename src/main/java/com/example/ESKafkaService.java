package com.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.errors.TopicExistsException;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ESKafkaService {

    public ESKafkaService() {
        String topicName = "es-kafka-sample-topic";
        ESKafkaCredentials credentials = ESKafkaUtil.getEventStreamsCredentials();
        String bootstrapServers = getCommaSeparated(credentials.getKafkaBrokersSasl());
        String apiKey = credentials.getApiKey();
        try (AdminClient admin = AdminClient.create(getAdminConfigs(bootstrapServers, apiKey))) {
            log.info("Creating the topic {}", topicName);
            NewTopic newTopic = new NewTopic(topicName, 1, (short) 3);
            CreateTopicsResult ctr = admin.createTopics(Collections.singleton(newTopic));
            ctr.all().get(10, TimeUnit.SECONDS);
        } catch (ExecutionException ee) {
            if (ee.getCause() instanceof TopicExistsException) {
                log.info("Topic {} already exists", topicName);
            } else {
                log.error("Error occurred creating the topic " + topicName, ee);
                System.exit(-1);
            }
        } catch (Exception e) {
            log.error("Error occurred creating the topic {}", topicName, e);
            System.exit(-1);
        }
        Map<String, Object> producerConfigs = getProducerConfigs(bootstrapServers, apiKey);

    }

    private Map<String, Object> getProducerConfigs(String bootstrapServers, String apikey) {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configs.put(ProducerConfig.CLIENT_ID_CONFIG, "es-kafka-sample-producer");
        configs.put(ProducerConfig.ACKS_CONFIG, "all");
        configs.put(ProducerConfig.CLIENT_DNS_LOOKUP_CONFIG, "use_all_dns_ips");
        configs.putAll(getCommonConfigs(bootstrapServers, apikey));
        return configs;
    }

    private Map<String, Object> getCommonConfigs(String boostrapServers, String apikey) {
        Map<String, Object> configs = new HashMap<>();
        configs.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, boostrapServers);
        configs.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
        configs.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        configs.put(SaslConfigs.SASL_JAAS_CONFIG, "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"token\" password=\"" + apikey + "\";");
        configs.put(SslConfigs.SSL_PROTOCOL_CONFIG, "TLSv1.2");
        configs.put(SslConfigs.SSL_ENABLED_PROTOCOLS_CONFIG, "TLSv1.2");
        configs.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "HTTPS");
        return configs;
    }

    private Properties getAdminConfigs(String bootstrapServers, String apikey) {
        Properties configs = new Properties();
        configs.put(ConsumerConfig.CLIENT_ID_CONFIG, "es-kafka-sample-admin");
        configs.put(AdminClientConfig.CLIENT_DNS_LOOKUP_CONFIG, "use_all_dns_ips");
        configs.putAll(getCommonConfigs(bootstrapServers, apikey));
        return configs;
    }

    private String getCommaSeparated(String[] sArray) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sArray.length; i++) {
            sb.append(sArray[i]);
            if (i < sArray.length - 1) sb.append(",");
        }
        return sb.toString();
    }
}
