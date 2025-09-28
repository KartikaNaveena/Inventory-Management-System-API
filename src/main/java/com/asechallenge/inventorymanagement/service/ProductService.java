package com.asechallenge.inventorymanagement.service;

import com.asechallenge.inventorymanagement.entity.Product;

public interface ProductService {

    // Create a new product
    Product createProduct(String name, String variant, String description,
                          Integer stockQuantity, Integer lowStockThreshold);
}