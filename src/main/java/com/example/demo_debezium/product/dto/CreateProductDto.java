package com.example.demo_debezium.product.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class CreateProductDto {
    private String name;
    private String type;
    private String color;
}
