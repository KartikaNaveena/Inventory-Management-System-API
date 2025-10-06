package com.asechallenge.inventorymanagement.dto;

import jakarta.validation.constraints.Min;

public class StockChangeRequestDTO {
    @Min(1)
    private int quantity;
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}