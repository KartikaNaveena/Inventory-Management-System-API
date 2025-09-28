package com.asechallenge.inventorymanagement.dto;

public class ProductResponseDTO {

    private String name;
    private String variant;
    private String description;
    private Integer stockQuantity;
    private Integer lowStockThreshold;
    private String sku;

    public ProductResponseDTO(String name, String variant, String description,
                              Integer stockQuantity, Integer lowStockThreshold, String sku) {
        this.name = name;
        this.variant = variant;
        this.description = description;
        this.stockQuantity = stockQuantity;
        this.lowStockThreshold = lowStockThreshold;
        this.sku = sku;
    }

    public String getName() { return name; }
    public String getVariant() { return variant; }
    public String getDescription() { return description; }
    public Integer getStockQuantity() { return stockQuantity; }
    public Integer getLowStockThreshold() { return lowStockThreshold; }
    public String getSku() { return sku; }
}
