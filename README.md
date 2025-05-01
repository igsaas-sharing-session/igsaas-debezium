# Debezium with PostgreSQL and Spring Boot

This project demonstrates how to set up **Change Data Capture (CDC)** with **Debezium** to capture changes in a PostgreSQL database and consume these changes in a Spring Boot application. The captured changes are then stored in **Redis** for quick access.

## Requirements

Enable CDC in PostgreSQL

1. **Docker** and **Docker Compose** for running the necessary services (Kafka, Zookeeper, PostgreSQL, Redis, Debezium).
2. **Spring Boot** application with **Spring Kafka**, **Spring Data Redis**, and **Debezium**.
3. **PostgreSQL** version >= 9.4 (for logical replication).

---

## Step 1: Set Up Docker Compose

The `docker-compose.yml` file defines the services required to run Kafka, Zookeeper, PostgreSQL, Redis, and Debezium.

```yaml
version: '3.8'

services:
  demo-debezium-zookeeper:
    image: confluentinc/cp-zookeeper:latest
    container_name: demo-debezium-zookeeper
    ports:
      - "2181:2181"
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181

  demo-debezium-kafka:
    image: confluentinc/cp-kafka:latest
    container_name: demo-debezium-kafka
    ports:
      - "9092:9092"
    environment:
      KAFKA_ZOOKEEPER_CONNECT: demo-debezium-zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://demo-debezium-kafka:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
    depends_on:
      - demo-debezium-zookeeper

  demo-debezium-postgres:
    image: postgres:latest
    container_name: demo-debezium-postgres
    environment:
      POSTGRES_USER: user
      POSTGRES_PASSWORD: password
      POSTGRES_DB: testdb
    ports:
      - "5432:5432"

  demo-debezium-redis:
    image: redis:latest
    container_name: demo-debezium-redis
    ports:
      - "6379:6379"

  demo-debezium-connect:
    image: debezium/connect:2.5
    container_name: demo-debezium-connect
    ports:
      - "8083:8083"
    environment:
      BOOTSTRAP_SERVERS: demo-debezium-kafka:9092
      GROUP_ID: 1
      CONFIG_STORAGE_TOPIC: debezium_config
      OFFSET_STORAGE_TOPIC: debezium_offset
      STATUS_STORAGE_TOPIC: debezium_status
    depends_on:
      - demo-debezium-kafka
      - demo-debezium-postgres

```

## Enable CDC in PostgreSQL
### Restart the container afterwards

```code
ALTER SYSTEM SET wal_level = logical;
```


