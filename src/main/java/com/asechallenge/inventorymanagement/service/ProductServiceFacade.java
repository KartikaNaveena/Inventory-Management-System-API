package com.asechallenge.inventorymanagement.service;

import org.springframework.stereotype.Service;
import com.asechallenge.inventorymanagement.entity.Product;

@Service
public class ProductServiceFacade implements ProductService {

    private final ProductCreationService creationService;

    public ProductServiceFacade(ProductCreationService creationService) {
        this.creationService = creationService;
    }

    @Override
    public Product createProduct(String name, String variant, String description,
                                 Integer stockQuantity, Integer lowStockThreshold) {
        return creationService.createProduct(name, variant, description, stockQuantity, lowStockThreshold);
    }
}
