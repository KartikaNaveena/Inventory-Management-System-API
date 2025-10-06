package com.asechallenge.inventorymanagement.exception;

public class DuplicateProductException extends InventoryException {
    public DuplicateProductException(String message) {
        super(message);
    }
}