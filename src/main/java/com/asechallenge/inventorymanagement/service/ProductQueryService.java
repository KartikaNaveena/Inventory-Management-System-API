package com.asechallenge.inventorymanagement.service;

import com.asechallenge.inventorymanagement.entity.Product;
import com.asechallenge.inventorymanagement.exception.ProductNotFoundException;
import com.asechallenge.inventorymanagement.repository.ProductRepository;
import com.asechallenge.inventorymanagement.util.SkuGenerator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductQueryService {

    private final ProductRepository productRepository;

    public ProductQueryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            throw new ProductNotFoundException("No products are currently available in the inventory.");
        }
        return products;
    }

    public Product getProductByNameAndVariant(String name, String variant) {
        String sku = SkuGenerator.generateSKU(name.trim(), variant.trim());
        return productRepository.findBySkuIgnoreCase(sku)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                            String.format("No product found for name '%s' and variant '%s'", name.trim(), variant.trim())
                        ));
    }

    public List<Product> getProductsByName(String name) {
        List<Product> products = productRepository.findProductsByNameContainingIgnoreCase(name.trim());
        if (products.isEmpty()) {
            throw new ProductNotFoundException(
                String.format("No products found matching name '%s'", name.trim())
            );
        }
        return products;
    }

    public List<Product> getLowStockProducts() {
        List<Product> products = productRepository.findLowStockProducts();
        if (products.isEmpty()) {
           throw new ProductNotFoundException("No products are below their low stock threshold.");
        }
        return products;
   }
}