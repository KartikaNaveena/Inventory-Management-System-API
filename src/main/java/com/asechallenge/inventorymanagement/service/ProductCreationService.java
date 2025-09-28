package com.asechallenge.inventorymanagement.service;

import org.springframework.stereotype.Service;
import com.asechallenge.inventorymanagement.entity.Product;
import com.asechallenge.inventorymanagement.repository.ProductRepository;
import com.asechallenge.inventorymanagement.exception.DuplicateProductException;

@Service
public class ProductCreationService {

    private final ProductRepository productRepository;

    public ProductCreationService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(String name, String variant, String description,
                                 Integer stockQuantity, Integer lowStockThreshold) {

        // Apply defaults if needed
        variant = defaultVariantIfNull(variant);
        stockQuantity = defaultStockIfNull(stockQuantity);

        // Check if product already exists
        if (isProductAlreadyPresent(name, variant)) {
            String variantDisplay = variant.isBlank() ? "<empty>" : variant;
            throw new DuplicateProductException(
                "Product with name '" + name + "' and variant '" + variantDisplay + "' already exists"
            );
        }

        // Generate SKU
        String sku = generateSKU(name, variant);

        // Create and save product
        Product product = new Product(name, variant, description, stockQuantity, lowStockThreshold, sku);
        return productRepository.save(product);
    }

    private String defaultVariantIfNull(String variant) {
        return (variant == null) ? "" : variant.trim();
    }

    private Integer defaultStockIfNull(Integer stockQuantity) {
        return (stockQuantity == null) ? 0 : stockQuantity;
    }

    private boolean isProductAlreadyPresent(String name, String variant) {
        return productRepository.existsProductByNameAndVariant(name, variant);
    }

    private String generateSKU(String name, String variant) {
        String base = name.replaceAll("\\s+", "").toLowerCase();
        if (variant.isBlank()) return base;
        String camelVariant = variant.substring(0,1).toUpperCase() + variant.substring(1).toLowerCase();
        return base + camelVariant;
    }
}