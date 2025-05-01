package com.example.demo_debezium.comsumer;

import com.example.demo_debezium.product.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DebeziumConsumer {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String REDIS_PRODUCT_PREFIX = "product::";

    @KafkaListener(topics = "dbserver1.public.product", groupId = "product-consumer-group")
    public void consume(String message) {
        try {
            KafkaProductMessageTemplate kafkaMessage = objectMapper.readValue(message, KafkaProductMessageTemplate.class);
            KafkaProductMessageTemplate.KafkaMessagePayload payload = kafkaMessage.getPayload();
            String operation = payload.getOp();

            switch (operation) {
                case "c": // create
                    handleCreate(payload.getAfter());
                    break;
                case "u": // update
                    handleUpdate(payload.getAfter());
                    break;
                case "d": // delete
                    handleDelete(payload.getBefore());
                    break;
                default:
                    System.out.println("Unknown operation type: " + operation);
            }

        } catch (Exception e) {
            System.err.println("Error processing Kafka message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleCreate(Product product) {
        if (product != null) {
            String redisKey = REDIS_PRODUCT_PREFIX + product.getId();
            redisTemplate.opsForValue().set(redisKey, product);
            System.out.println("Created in Redis: " + redisKey);
        }
    }

    private void handleUpdate(Product product) {
        if (product != null) {
            String redisKey = REDIS_PRODUCT_PREFIX + product.getId();
            redisTemplate.opsForValue().set(redisKey, product);
            System.out.println("Updated in Redis: " + redisKey);
        }
    }

    private void handleDelete(Product product) {
        if (product != null) {
            String redisKey = REDIS_PRODUCT_PREFIX + product.getId();
            redisTemplate.delete(redisKey);
            System.out.println("Deleted from Redis: " + redisKey);
        }
    }

}
