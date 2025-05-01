package com.example.demo_debezium.comsumer;

import com.example.demo_debezium.product.model.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class KafkaProductMessageTemplate {

    private KafkaMessagePayload payload;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class KafkaMessagePayload{
        private Product before;
        private Product after;
        private String op;
    }
}

