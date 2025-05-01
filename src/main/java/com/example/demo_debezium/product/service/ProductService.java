package com.example.demo_debezium.product.service;

import com.example.demo_debezium.product.dto.CreateProductDto;
import com.example.demo_debezium.product.model.Product;
import com.example.demo_debezium.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.connect.errors.NotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> findAll(){
        return productRepository.findAll();
    }

    public Product save(CreateProductDto createProductDto){
        Product product = new Product();
        product.setName(createProductDto.getName());
        product.setType(createProductDto.getType());
        product.setColor(createProductDto.getColor());
        return productRepository.save(product);
    }

    @Cacheable(value = "product", key = "#id")
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }
}
