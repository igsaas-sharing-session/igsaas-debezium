package com.example.demo_debezium.product.controller;

import com.example.demo_debezium.product.dto.CreateProductDto;
import com.example.demo_debezium.product.model.Product;
import com.example.demo_debezium.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping()
    public List<Product> findAll(){
        return productService.findAll();
    }

    @PostMapping()
    public Product save(@RequestBody  CreateProductDto createProductDto){
        return productService.save(createProductDto);
    }

    @GetMapping("/{id}")
    public Product getProductService(@PathVariable("id") Long id) {
        return productService.findById(id);
    }
}
