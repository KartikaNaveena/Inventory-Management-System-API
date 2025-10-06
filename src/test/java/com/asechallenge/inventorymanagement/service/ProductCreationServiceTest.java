package com.asechallenge.inventorymanagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.asechallenge.inventorymanagement.dto.ProductCreationRequestDTO;
import com.asechallenge.inventorymanagement.entity.Product;
import com.asechallenge.inventorymanagement.exception.DuplicateProductException;
import com.asechallenge.inventorymanagement.repository.ProductRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

public class ProductCreationServiceTest {

    private ProductRepository productRepository;
    private ProductCreationService creationService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        creationService = new ProductCreationService(productRepository);
    }

    @Test
    void createProduct_success_shouldReturnProduct() {
        ProductCreationRequestDTO dto = new ProductCreationRequestDTO();
        dto.setName("Keyboard");
        dto.setVariant("Mechanical");
        dto.setDescription("Gaming keyboard");
        dto.setStockQuantity(15);
        dto.setLowStockThreshold(5);

        when(productRepository.findBySkuIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        Product product = creationService.createProduct(dto);

        assertEquals("Keyboard", product.getName());
        assertEquals("Mechanical", product.getVariant());
        assertEquals(15, product.getStockQuantity());
        assertEquals("Gaming keyboard", product.getDescription());
        assertEquals(5, product.getLowStockThreshold());
    }

    @Test
    void createProduct_duplicate_shouldThrowException() {
        ProductCreationRequestDTO dto = new ProductCreationRequestDTO();
        dto.setName("Laptop");
        dto.setVariant("XPS13");
        dto.setDescription("Dell ultrabook");
        dto.setStockQuantity(10);
        dto.setLowStockThreshold(3);

        when(productRepository.findBySkuIgnoreCase(anyString())).thenReturn(Optional.of(new Product()));

        assertThrows(DuplicateProductException.class, () -> creationService.createProduct(dto));
    }

    @Test
    void createProduct_nullStock_shouldDefaultToZero() {
        ProductCreationRequestDTO request = new ProductCreationRequestDTO();
        request.setName("Mouse");
        request.setVariant(null);
        request.setDescription("desc");
        request.setStockQuantity(null);
        request.setLowStockThreshold(3);

        when(productRepository.findBySkuIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        Product p = creationService.createProduct(request);
        assertEquals(0, p.getStockQuantity());
    }
}
