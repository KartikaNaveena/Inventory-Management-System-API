package com.asechallenge.inventorymanagement.service;

import org.springframework.stereotype.Service;

import com.asechallenge.inventorymanagement.exception.ProductNotFoundException;
import com.asechallenge.inventorymanagement.util.SkuGenerator;
import com.asechallenge.inventorymanagement.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductDeleteService {
    private final ProductRepository productRepository;

    public ProductDeleteService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Transactional
    public boolean deleteProduct(String name, String variant) {
        String sku = SkuGenerator.generateSKU(name.trim(), variant.trim());

        int deletedRows = productRepository.deleteBySkuIgnoreCase(sku);

        if (deletedRows == 0) {
            throw new ProductNotFoundException(
                String.format("Cannot delete product. Product with name '%s' and variant '%s' not found.", 
                            name, variant)
            );
        }

        return true;
    } 
}
