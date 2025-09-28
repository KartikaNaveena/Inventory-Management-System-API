package com.asechallenge.inventorymanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "products",
    uniqueConstraints = @UniqueConstraint(columnNames = {"name", "variant"})
)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String name;

    @Column(nullable = false, updatable = false)
    private String variant;

    @Column(nullable = false, unique = true, updatable = false)
    private String sku;

    @Column
    private String description;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Column(nullable = false, updatable = false)
    private Integer lowStockThreshold; 

    public Product() {}

    public Product(String name, String variant, String description,
                   Integer stockQuantity, Integer lowStockThreshold, String sku) {
        this.name = name;
        this.variant = variant;
        this.description = description;
        this.stockQuantity = stockQuantity;
        this.lowStockThreshold = lowStockThreshold;
        this.sku = sku;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getVariant() { return variant; }
    public String getSku() { return sku; }
    public String getDescription() { return description; }
    public Integer getStockQuantity() { return stockQuantity; }
    public Integer getLowStockThreshold() { return lowStockThreshold; }

    public void setDescription(String description) { this.description = description; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
}