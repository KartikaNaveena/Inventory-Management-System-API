package com.asechallenge.inventorymanagement.service;

import org.springframework.stereotype.Service;

import com.asechallenge.inventorymanagement.dto.ProductUpdateRequestDTO;
import com.asechallenge.inventorymanagement.dto.StockChangeRequestDTO;
import com.asechallenge.inventorymanagement.entity.Product;
import com.asechallenge.inventorymanagement.repository.ProductRepository;
import com.asechallenge.inventorymanagement.util.SkuGenerator;

import jakarta.transaction.Transactional;

import com.asechallenge.inventorymanagement.exception.InvalidStockValueException;
import com.asechallenge.inventorymanagement.exception.ProductNotFoundException;

@Service
public class ProductUpdateService {

    private final ProductRepository productRepository;

    public ProductUpdateService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

     @Transactional
    public Product updateProduct(String name, String variant, ProductUpdateRequestDTO requestDTO) {
        if (requestDTO.getDescription() == null || requestDTO.getDescription().isBlank()) {
            throw new IllegalArgumentException("No value provided for update.");
        }

        String sku = SkuGenerator.generateSKU(name.trim(), variant.trim());

        int updatedRows = productRepository.updateDescriptionBySkuIgnoreCase(sku, requestDTO.getDescription());
        if (updatedRows == 0) {
            throw new ProductNotFoundException(
                String.format("Cannot update description. Product not found for name '%s' and variant '%s'", name, variant)
            );
        }

        return productRepository.findBySkuIgnoreCase(sku)
                .orElseThrow(() -> new ProductNotFoundException(
                        String.format("Product not found after update for name '%s' and variant '%s'", name, variant)
                ));
    }

    @Transactional
    public Product increaseStock(String name, String variant, StockChangeRequestDTO requestDTO) {
        if (requestDTO.getQuantity() <= 0) {
            throw new InvalidStockValueException("Quantity to increase must be greater than 0 and an Integer");
        }

        String sku = SkuGenerator.generateSKU(name.trim(), variant.trim());

        int updatedRows = productRepository.increaseStockBySkuIgnoreCase(sku, requestDTO.getQuantity());
        if (updatedRows == 0) {
            throw new ProductNotFoundException(
                        String.format("Product with name '%s' and variant '%s' not found", name, variant)
                );
        }

        return productRepository.findBySkuIgnoreCase(sku).get();
    }

    @Transactional
    public Product decreaseStock(String name, String variant, StockChangeRequestDTO requestDTO) {
        if (requestDTO.getQuantity() <= 0) {
            throw new InvalidStockValueException("Quantity to increase must be greater than 0 and an Integer");
        }

        String sku = SkuGenerator.generateSKU(name.trim(), variant.trim());

        // Check if product exists
        Product product = productRepository.findBySkuIgnoreCase(sku)
                .orElseThrow(() -> new ProductNotFoundException(
                        String.format("Product with name '%s' and variant '%s' not found", name, variant)
                ));

        if (product.getStockQuantity() < requestDTO.getQuantity()) {
            throw new InvalidStockValueException(
                    String.format("Cannot decrease stock by %d. Only %d items available.", 
                            requestDTO.getQuantity(), product.getStockQuantity())
            );
        }

        // Decrease stock
        productRepository.decreaseStockBySkuIgnoreCase(sku, requestDTO.getQuantity());

        return productRepository.findBySkuIgnoreCase(sku).get();
    }

}