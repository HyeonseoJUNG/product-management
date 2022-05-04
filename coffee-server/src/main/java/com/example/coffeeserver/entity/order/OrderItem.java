package com.example.coffeeserver.entity.order;

import com.example.coffeeserver.entity.product.Category;

import java.time.LocalDateTime;
import java.util.UUID;

public class OrderItem {
    private final UUID productId;
    private final Category category;
    private final long price;
    private final int quantity;
    private final LocalDateTime createdAt;

    public OrderItem(UUID productId, Category category, long price, int quantity) {
        this.productId = productId;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getProductId() {
        return productId;
    }

    public Category getCategory() {
        return category;
    }

    public long getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}