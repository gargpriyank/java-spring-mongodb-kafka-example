package com.springboot.sample;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ESKafkaCredentials {

    @JsonProperty("api_key")
    private String apiKey;

    private String user;

    private String password;

    @JsonProperty("kafka_brokers_sasl")
    private String[] kafkaBrokersSasl;
}
