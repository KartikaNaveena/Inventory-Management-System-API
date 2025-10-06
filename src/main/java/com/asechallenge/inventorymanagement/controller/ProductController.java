package com.asechallenge.inventorymanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.asechallenge.inventorymanagement.dto.ProductCreationRequestDTO;
import com.asechallenge.inventorymanagement.dto.ProductResponseDTO;
import com.asechallenge.inventorymanagement.dto.ProductUpdateRequestDTO;
import com.asechallenge.inventorymanagement.dto.StockChangeRequestDTO;
import com.asechallenge.inventorymanagement.entity.Product;
import com.asechallenge.inventorymanagement.service.ProductService;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductResponseDTO> response = productService.getAllProducts()
                .stream()
                .map(this::toResponseDTO)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping(params = "name")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByName(@RequestParam String name) {
        List<ProductResponseDTO> response = productService.getProductsByName(name)
                .stream()
                .map(this::toResponseDTO)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping(params = {"name", "variant"})
    public ResponseEntity<ProductResponseDTO> getProductByNameAndVariant(
            @RequestParam String name,
            @RequestParam String variant) {
        Product product = productService.getProductByNameAndVariant(name, variant);
        return ResponseEntity.ok(toResponseDTO(product));
    }

    @GetMapping(params = "lowStock")
    public ResponseEntity<List<ProductResponseDTO>> getLowStockProducts(
            @RequestParam Boolean lowStock) {
        if (Boolean.TRUE.equals(lowStock)) {
            List<ProductResponseDTO> response = productService.getLowStockProducts()
                    .stream()
                    .map(this::toResponseDTO)
                    .toList();
            return ResponseEntity.ok(response);
        }
        return getAllProducts();
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(
            @Valid @RequestBody ProductCreationRequestDTO request) {

        Product product = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponseDTO(product));
    }


    @PatchMapping(params = {"name", "variant"})
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @RequestParam String name,
            @RequestParam String variant,
            @Valid @RequestBody ProductUpdateRequestDTO request) {
        Product updated = productService.updateProduct(name, variant, request);
        return ResponseEntity.ok(toResponseDTO(updated));
    }

    @PatchMapping(params = {"name", "variant"}, path = "/stock/increase")
    public ResponseEntity<ProductResponseDTO> increaseStock(
            @RequestParam String name,
            @RequestParam String variant,
            @Valid @RequestBody StockChangeRequestDTO request) {
        Product updated = productService.increaseProduct(name, variant, request);
        return ResponseEntity.ok(toResponseDTO(updated));
    }

    @PatchMapping(params = {"name", "variant"}, path = "/stock/decrease")
    public ResponseEntity<ProductResponseDTO> decreaseStock(
            @RequestParam String name,
            @RequestParam String variant,
            @Valid @RequestBody StockChangeRequestDTO request) {
        Product updated = productService.decreaseProduct(name, variant, request);
        return ResponseEntity.ok(toResponseDTO(updated));
    }

    @DeleteMapping(params = {"name", "variant"})
    public ResponseEntity<Void> deleteProduct(
            @RequestParam String name,
            @RequestParam String variant) {

        productService.deleteProduct(name, variant);

        return ResponseEntity.noContent().build();
    }

    private ProductResponseDTO toResponseDTO(Product p) {
        return new ProductResponseDTO(
            p.getName(),
            p.getVariant(),
            p.getDescription(),
            p.getStockQuantity(),
            p.getLowStockThreshold(),
            p.getSku()
        );
    }
}