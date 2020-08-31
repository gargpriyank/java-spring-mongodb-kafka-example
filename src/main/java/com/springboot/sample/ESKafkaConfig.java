package com.springboot.sample;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@EnableKafka
public class ESKafkaConfig {

    @Value("${ES_KAFKA_TOPIC_NAME}")
    private String topicName;

    @Autowired
    private ESKafkaUtil esKafkaUtil;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        return new KafkaAdmin(getAdminConfigs(getBootstrapServers(), getApiKey()));
    }

    @Bean
    public NewTopic sampleTopic() {
        return new NewTopic(topicName, 1, (short) 1);
    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(getProducerConfigs());
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(getConsumerConfigs());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    private Map<String, Object> getProducerConfigs() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configs.put(ProducerConfig.CLIENT_ID_CONFIG, "es-kafka-sample-producer");
        configs.put(ProducerConfig.ACKS_CONFIG, "all");
        configs.put(ProducerConfig.CLIENT_DNS_LOOKUP_CONFIG, "use_all_dns_ips");
        configs.putAll(getCommonConfigs(getBootstrapServers(), getApiKey()));
        return configs;
    }

    private Map<String, Object> getConsumerConfigs() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        configs.put(ConsumerConfig.CLIENT_ID_CONFIG, "es-kafka-sample-consumer");
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, "es-kafka-sample-group");
        configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        configs.put(ConsumerConfig.CLIENT_DNS_LOOKUP_CONFIG, "use_all_dns_ips");
        configs.putAll(getCommonConfigs(getBootstrapServers(), getApiKey()));
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

    private Map<String, Object> getAdminConfigs(String bootstrapServers, String apikey) {
        Map<String, Object> configs = new HashMap<>();
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

    private String getBootstrapServers() {
        ESKafkaCredentials credentials = esKafkaUtil.getEventStreamsCredentials();
        return getCommaSeparated(credentials.getKafkaBrokersSasl());
    }

    private String getApiKey() {
        ESKafkaCredentials credentials = esKafkaUtil.getEventStreamsCredentials();
        return credentials.getApiKey();
    }
}
