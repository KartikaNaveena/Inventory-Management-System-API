package com.asechallenge.inventorymanagement.service;

import com.asechallenge.inventorymanagement.entity.Product;
import com.asechallenge.inventorymanagement.exception.ProductNotFoundException;
import com.asechallenge.inventorymanagement.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void getAllProducts_success_shouldReturnList() {
        List<Product> products = new ArrayList<>();
        products.add(new Product("Laptop", "13inch", "desc", 5, 2, "laptop13Inch"));

        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = queryService.getAllProducts();
        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getName());
    }

    @Test
    void getProductsByName_success_shouldReturnList() {
        List<Product> products = new ArrayList<>();
        products.add(new Product("Mouse", null, "desc", 10, 3, "mouse"));

        when(productRepository.findProductsByNameContainingIgnoreCase("Mouse")).thenReturn(products);

        List<Product> result = queryService.getProductsByName("Mouse");
        assertEquals(1, result.size());
        assertEquals("Mouse", result.get(0).getName());
    }

    @Test
    void getProductByNameAndVariant_success_shouldReturnProduct() {
        Product product = new Product("Laptop", "13inch", "desc", 5, 2, "laptop13Inch");

        when(productRepository.findBySkuIgnoreCase("laptop13inch")).thenReturn(Optional.of(product));

        Product result = queryService.getProductByNameAndVariant("Laptop", "13inch");
        assertEquals("Laptop", result.getName());
        assertEquals("13inch", result.getVariant());
    }

    @Test
    void getLowStockProducts_success_shouldReturnList() {
        List<Product> products = new ArrayList<>();
        products.add(new Product("Keyboard", "Mechanical", "desc", 2, 5, "keyboardMechanical"));

        when(productRepository.findLowStockProducts()).thenReturn(products);

        List<Product> result = queryService.getLowStockProducts();
        assertEquals(1, result.size());
        assertEquals("Keyboard", result.get(0).getName());
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