package com.asechallenge.inventorymanagement.service;

import org.springframework.stereotype.Service;

import com.asechallenge.inventorymanagement.dto.ProductCreationRequestDTO;
import com.asechallenge.inventorymanagement.entity.Product;
import com.asechallenge.inventorymanagement.repository.ProductRepository;
import com.asechallenge.inventorymanagement.util.SkuGenerator;
import com.asechallenge.inventorymanagement.exception.DuplicateProductException;

@Service
public class ProductCreationService {

    private final ProductRepository productRepository;

    public ProductCreationService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(ProductCreationRequestDTO request) {
        String name = normalizeName(request.getName());
        String variant = normalizeVariant(request.getVariant());
        Long stockQuantity = defaultStockIfNull(request.getStockQuantity());
        Long lowStockThreshold = request.getLowStockThreshold();

        String sku = SkuGenerator.generateSKU(name.trim(), variant.trim());

        checkDuplicate(sku, name, variant);

        Product product = new Product(name, variant, request.getDescription(),
                                      stockQuantity, lowStockThreshold, sku);

        return productRepository.save(product);
    }

    private String normalizeName(String name) {
        return name.trim();
    }

    private String normalizeVariant(String variant) {
        return (variant == null) ? "" : variant.trim();
    }

    private Long defaultStockIfNull(Long stockQuantity) {
        return (stockQuantity == null) ? 0L : stockQuantity;
    }

    private void checkDuplicate(String sku, String name, String variant) {
        if (productRepository.findBySkuIgnoreCase(sku).isPresent()) {
            throw new DuplicateProductException(
                    String.format("Product with given name and variant already exists")
            );
        }
    }
}