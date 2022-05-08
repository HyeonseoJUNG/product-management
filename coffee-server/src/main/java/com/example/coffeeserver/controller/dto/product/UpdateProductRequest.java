package com.example.coffeeserver.controller.dto.product;

public record UpdateProductRequest(String productName,
                                   String category,
                                   long price,
                                   String description) {
}
