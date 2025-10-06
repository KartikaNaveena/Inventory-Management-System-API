package com.asechallenge.inventorymanagement.service;

import org.springframework.stereotype.Service;

import com.asechallenge.inventorymanagement.dto.ProductCreationRequestDTO;
import com.asechallenge.inventorymanagement.dto.ProductUpdateRequestDTO;
import com.asechallenge.inventorymanagement.dto.StockChangeRequestDTO;
import com.asechallenge.inventorymanagement.entity.Product;

import java.util.List;

@Service
public class ProductServiceFacade implements ProductService {

    private final ProductCreationService creationService;
    private final ProductQueryService queryService;
    private final ProductUpdateService updateService;
    private final ProductDeleteService deleteService;

    public ProductServiceFacade(ProductCreationService creationService,
                                ProductQueryService queryService,
                                ProductUpdateService updateService,
                                ProductDeleteService deleteService) {
        this.creationService = creationService;
        this.queryService = queryService;
        this.updateService = updateService;
        this.deleteService = deleteService;
    }

    @Override
    public Product createProduct(ProductCreationRequestDTO request) {
        return creationService.createProduct(request);
    }

    @Override
    public List<Product> getAllProducts() {
        return queryService.getAllProducts();
    }

    @Override
    public Product getProductByNameAndVariant(String name, String variant) {
        return queryService.getProductByNameAndVariant(name, variant);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return queryService.getProductsByName(name);
    }

    @Override
    public List<Product> getLowStockProducts() {
        return queryService.getLowStockProducts();
    }

     @Override
    public Product updateProduct(String name, String variant, ProductUpdateRequestDTO requestDTO) {
        return updateService.updateProduct(name, variant, requestDTO);
    }

    @Override
    public Product increaseProduct(String name, String variant, StockChangeRequestDTO requestDTO) {
        return updateService.increaseStock(name, variant, requestDTO);
    }

    @Override
    public Product decreaseProduct(String name, String variant, StockChangeRequestDTO requestDTO) {
        return updateService.decreaseStock(name, variant, requestDTO);
    }

    @Override
    public boolean deleteProduct(String name, String variant){
        return deleteService.deleteProduct(name, variant);
    }
}