package com.asechallenge.inventorymanagement.exception;

public class ProductNotFoundException extends InventoryException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}