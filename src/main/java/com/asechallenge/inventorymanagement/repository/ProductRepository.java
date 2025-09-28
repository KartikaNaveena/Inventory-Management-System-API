package com.asechallenge.inventorymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.asechallenge.inventorymanagement.entity.Product;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Check if a product exists with the same name and variant
    boolean existsProductByNameAndVariant(String name, String variant);

    // Search for products containing the given name (case-insensitive)
    List<Product> findProductsByNameContainingIgnoreCase(String name);
}