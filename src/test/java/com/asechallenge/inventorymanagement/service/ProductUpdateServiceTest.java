package com.asechallenge.inventorymanagement.service;

import com.asechallenge.inventorymanagement.dto.ProductUpdateRequestDTO;
import com.asechallenge.inventorymanagement.dto.StockChangeRequestDTO;
import com.asechallenge.inventorymanagement.entity.Product;
import com.asechallenge.inventorymanagement.exception.InvalidStockValueException;
import com.asechallenge.inventorymanagement.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;

public class ProductUpdateServiceTest {

    private ProductRepository productRepository;
    private ProductUpdateService updateService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        updateService = new ProductUpdateService(productRepository);
    }

    @Test
    void updateProduct_blankDescription_shouldThrowException() {
        ProductUpdateRequestDTO dto = new ProductUpdateRequestDTO();
        dto.setDescription("   ");
        assertThrows(IllegalArgumentException.class, () -> 
            updateService.updateProduct("Laptop", "13inch", dto)
        );
    }

    @Test
    void updateProduct_nullDescription_shouldThrowException() {
        ProductUpdateRequestDTO dto = new ProductUpdateRequestDTO();
        dto.setDescription(null);
        assertThrows(IllegalArgumentException.class, () -> 
            updateService.updateProduct("Laptop", "13inch", dto)
        );
    }

    @Test
    void decreaseStock_moreThanAvailable_shouldThrowException() {
        Product product = new Product("Laptop", "13inch", "desc", 5L, 2L, "laptop13Inch");
        when(productRepository.findBySkuIgnoreCase(anyString())).thenReturn(Optional.of(product));
        StockChangeRequestDTO dto = new StockChangeRequestDTO();
        dto.setQuantity(10L);

        assertThrows(InvalidStockValueException.class, () -> 
            updateService.decreaseStock("Laptop", "13inch", dto)
        );
    }
}