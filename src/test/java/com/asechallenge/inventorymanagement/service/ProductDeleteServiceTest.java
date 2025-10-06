package com.asechallenge.inventorymanagement.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;

import com.asechallenge.inventorymanagement.exception.ProductNotFoundException;
import com.asechallenge.inventorymanagement.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProductDeleteServiceTest {

    private ProductRepository productRepository;
    private ProductDeleteService deleteService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        deleteService = new ProductDeleteService(productRepository);
    }

    @Test
    void deleteProduct_success_shouldReturnTrue() {
        when(productRepository.deleteBySkuIgnoreCase(anyString())).thenReturn(1);

        boolean result = deleteService.deleteProduct("Laptop", "13inch");

        assertTrue(result);
    }

    @Test
    void deleteProduct_notFound_shouldThrowException() {
        when(productRepository.deleteBySkuIgnoreCase(anyString())).thenReturn(0);

        assertThrows(ProductNotFoundException.class, () -> 
            deleteService.deleteProduct("Laptop", "13inch")
        );
    }
}