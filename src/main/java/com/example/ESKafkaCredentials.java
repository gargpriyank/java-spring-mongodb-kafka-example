package com.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ESKafkaCredentials {

    private String apiKey;
    private String user;
    private String password;
    private String[] kafkaBrokersSasl;

    @JsonProperty("api_key")
    public String getApiKey() {
        return apiKey;
    }

    @JsonProperty("api_key")
    public void setLabel(String apiKey) {
        this.apiKey = apiKey;
    }

    @JsonProperty
    public String getUser() {
        return user;
    }

    @JsonProperty
    public void setUser(String user) {
        this.user = user;
    }

    @JsonProperty
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("kafka_brokers_sasl")
    public String[] getKafkaBrokersSasl() {
        return kafkaBrokersSasl;
    }

    @JsonProperty("kafka_brokers_sasl")
    public void setKafkaBrokersSasl(String[] kafkaBrokersSasl) {
        this.kafkaBrokersSasl = kafkaBrokersSasl;
    }
}
