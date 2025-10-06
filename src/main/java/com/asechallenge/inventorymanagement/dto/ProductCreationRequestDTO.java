package com.asechallenge.inventorymanagement.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductCreationRequestDTO {

    @NotBlank(message = "Product name is required")
    private String name;

    private String variant;

    private String description;

    @Min(value = 0, message = "Stock quantity must be >= 0")
    private Long stockQuantity; 

    @NotNull(message = "Low stock threshold is required")
    @Min(value = 1, message = "Low stock threshold must be > 0")
    private Long lowStockThreshold;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getVariant() { return variant; }
    public void setVariant(String variant) { this.variant = variant; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Long stockQuantity) { this.stockQuantity = stockQuantity; }

    public Long getLowStockThreshold() { return lowStockThreshold; }
    public void setLowStockThreshold(Long lowStockThreshold) { this.lowStockThreshold = lowStockThreshold; }
}
