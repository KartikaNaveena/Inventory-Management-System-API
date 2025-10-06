package com.asechallenge.inventorymanagement.service;

import com.asechallenge.inventorymanagement.exception.ProductNotFoundException;
import com.asechallenge.inventorymanagement.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductQueryServiceTest {

    private ProductRepository productRepository;
    private ProductQueryService queryService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        queryService = new ProductQueryService(productRepository);
    }

    @Test
    void getAllProducts_emptyList_shouldThrowException() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(ProductNotFoundException.class, () -> queryService.getAllProducts());
    }

    @Test
    void getProductsByName_noMatch_shouldThrowException() {
        when(productRepository.findProductsByNameContainingIgnoreCase("NonExistent"))
                .thenReturn(Collections.emptyList());

        assertThrows(ProductNotFoundException.class, () -> queryService.getProductsByName("NonExistent"));
    }

    @Test
    void getProductByNameAndVariant_notFound_shouldThrowException() {
        when(productRepository.findBySkuIgnoreCase("laptop13inch")).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> queryService.getProductByNameAndVariant("Laptop", "13inch"));
    }

    @Test
    void getLowStockProducts_emptyList_shouldThrowException() {
        when(productRepository.findLowStockProducts()).thenReturn(Collections.emptyList());

        assertThrows(ProductNotFoundException.class, () -> queryService.getLowStockProducts());
    }
}