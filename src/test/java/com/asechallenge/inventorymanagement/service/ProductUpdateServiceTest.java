package com.asechallenge.inventorymanagement.service;

import com.asechallenge.inventorymanagement.dto.ProductUpdateRequestDTO;
import com.asechallenge.inventorymanagement.dto.StockChangeRequestDTO;
import com.asechallenge.inventorymanagement.entity.Product;
import com.asechallenge.inventorymanagement.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

public class ProductUpdateServiceTest {

    private ProductRepository productRepository;
    private ProductUpdateService updateService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        updateService = new ProductUpdateService(productRepository);
    }

    @Test
    void increaseStock_validRequest_shouldReturnUpdatedProduct() {
        Product product = new Product("Laptop", "13inch", "desc", 5, 2, "laptop13Inch");
        StockChangeRequestDTO dto = new StockChangeRequestDTO();
        dto.setQuantity(3);

        when(productRepository.increaseStockBySkuIgnoreCase(anyString(), anyInt())).thenReturn(1);
        when(productRepository.findBySkuIgnoreCase(anyString())).thenReturn(Optional.of(product));

        Product updated = updateService.increaseStock("Laptop", "13inch", dto);

        assertEquals("Laptop", updated.getName());
        assertEquals(5, updated.getStockQuantity());
        verify(productRepository, times(1)).increaseStockBySkuIgnoreCase(anyString(), anyInt());
    }

    @Test
    void decreaseStock_validRequest_shouldReturnUpdatedProduct() {
        Product product = new Product("Laptop", "13inch", "desc", 10, 2, "laptop13Inch");
        StockChangeRequestDTO dto = new StockChangeRequestDTO();
        dto.setQuantity(4);

        when(productRepository.findBySkuIgnoreCase(anyString())).thenReturn(Optional.of(product));
        when(productRepository.decreaseStockBySkuIgnoreCase(anyString(), anyInt())).thenReturn(1);
        when(productRepository.findBySkuIgnoreCase(anyString())).thenReturn(Optional.of(product));

        Product updated = updateService.decreaseStock("Laptop", "13inch", dto);

        assertEquals("Laptop", updated.getName());
        assertEquals(10, updated.getStockQuantity());
        verify(productRepository, times(1)).decreaseStockBySkuIgnoreCase(anyString(), anyInt());
    }

    @Test
    void updateProduct_validDescription_shouldReturnUpdatedProduct() {
        Product product = new Product("Laptop", "13inch", "Old desc", 5, 2, "laptop13Inch");
        ProductUpdateRequestDTO dto = new ProductUpdateRequestDTO();
        dto.setDescription("New desc");

        when(productRepository.updateDescriptionBySkuIgnoreCase(anyString(), anyString())).thenReturn(1);
        when(productRepository.findBySkuIgnoreCase(anyString())).thenReturn(Optional.of(product));

        Product updated = updateService.updateProduct("Laptop", "13inch", dto);

        assertEquals("Laptop", updated.getName());
        assertEquals("Old desc", updated.getDescription());
        verify(productRepository, times(1)).updateDescriptionBySkuIgnoreCase(anyString(), anyString());
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
    void decreaseStock_moreThanAvailable_shouldThrowException() {
        Product product = new Product("Laptop", "13inch", "desc", 5, 2, "laptop13Inch");
        StockChangeRequestDTO dto = new StockChangeRequestDTO();
        dto.setQuantity(10);

        when(productRepository.findBySkuIgnoreCase(anyString())).thenReturn(Optional.of(product));

        assertThrows(IllegalArgumentException.class, () -> 
            updateService.decreaseStock("Laptop", "13inch", dto)
        );
    }
}