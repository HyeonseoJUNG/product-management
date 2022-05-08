package com.example.coffeeserver.controller.dto.product;

import com.example.coffeeserver.entity.product.Product;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProductsResponse {
    private final UUID productId;
    private final String productName;
    private final String category;
    private final long price;
    private final String description;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;


    public ProductsResponse(UUID productId, String productName, String category, long price, String description, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public ProductsResponse(Product product) {
        this.productId = product.getProductId();
        this.productName = product.getProductName();
        this.category = product.getCategory().name();
        this.price = product.getPrice();
        this.description = product.getDescription();
        this.createdAt = product.getCreatedAt();
        this.updatedAt = product.getUpdatedAt();
    }

    public UUID getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getCategory() {
        return category;
    }

    public long getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
