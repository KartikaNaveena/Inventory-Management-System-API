package com.asechallenge.inventorymanagement.dto;

import jakarta.validation.constraints.Min;

public class StockChangeRequestDTO {
    @Min(1)
    private Long quantity;
    
    public Long getQuantity() { return quantity; }
    public void setQuantity(Long quantity) { this.quantity = quantity; }
}