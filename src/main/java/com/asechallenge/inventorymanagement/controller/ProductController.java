package com.asechallenge.inventorymanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.asechallenge.inventorymanagement.dto.ProductRequestDTO;
import com.asechallenge.inventorymanagement.dto.ProductResponseDTO;
import com.asechallenge.inventorymanagement.entity.Product;
import com.asechallenge.inventorymanagement.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO request) {
        Product product = productService.createProduct(
            request.getName(),
            request.getVariant(),
            request.getDescription(),
            request.getStockQuantity(),
            request.getLowStockThreshold()
        );

        // Map entity to response DTO
        ProductResponseDTO response = new ProductResponseDTO(
            product.getName(),
            product.getVariant(),
            product.getDescription(),
            product.getStockQuantity(),
            product.getLowStockThreshold(),
            product.getSku()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}