package com.asechallenge.inventorymanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.asechallenge.inventorymanagement.entity.Product;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findProductsByNameContainingIgnoreCase(String name);

    Optional<Product> findBySkuIgnoreCase(String sku);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Product p SET p.description = :description WHERE p.sku = :sku")
    int updateDescriptionBySkuIgnoreCase(@Param("sku") String sku, @Param("description") String description);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Product p SET p.stockQuantity = p.stockQuantity + :quantity WHERE p.sku = :sku")
    int increaseStockBySkuIgnoreCase(@Param("sku") String sku, @Param("quantity") int quantity);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Product p SET p.stockQuantity = p.stockQuantity - :quantity WHERE p.sku = :sku AND p.stockQuantity >= :quantity")
    int decreaseStockBySkuIgnoreCase(@Param("sku") String sku, @Param("quantity") int quantity);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("DELETE FROM Product p WHERE LOWER(p.sku) = LOWER(:sku)")
    int deleteBySkuIgnoreCase(@Param("sku") String sku);

    
    @Query("SELECT p FROM Product p WHERE p.stockQuantity < p.lowStockThreshold")
    List<Product> findLowStockProducts();
}