package com.example.coffeeserver.controller.dto.order;

import com.example.coffeeserver.entity.order.OrderItem;
import com.example.coffeeserver.entity.product.Category;

import java.util.UUID;

public class OrderItemRequest {
    private final UUID productId;
    private final Category category;
    private final long price;
    private final int quantity;

    public OrderItemRequest(UUID productId, Category category, long price, int quantity) {
        this.productId = productId;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    public OrderItem toEntity() {
        return new OrderItem(productId, category, price, quantity);
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
}
