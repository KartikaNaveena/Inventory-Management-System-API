package com.asechallenge.inventorymanagement.service;

import com.asechallenge.inventorymanagement.dto.ProductCreationRequestDTO;
import com.asechallenge.inventorymanagement.dto.ProductUpdateRequestDTO;
import com.asechallenge.inventorymanagement.dto.StockChangeRequestDTO;
import com.asechallenge.inventorymanagement.entity.Product;
import java.util.List;

public interface ProductService {
    Product createProduct(ProductCreationRequestDTO requestDTO);

    List<Product> getAllProducts();

    Product getProductByNameAndVariant(String name, String variant);

    List<Product> getProductsByName(String name);

    Product updateProduct(String name, String variant, ProductUpdateRequestDTO requestDTO);

    Product increaseProduct(String name, String variant, StockChangeRequestDTO requestDTO);

    Product decreaseProduct(String name, String variant, StockChangeRequestDTO requestDTO);

    List<Product> getLowStockProducts();

    boolean deleteProduct(String name, String variant);
}