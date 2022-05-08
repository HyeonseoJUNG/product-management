package com.example.coffeeserver.controller.dto.product;

public record NewProductRequest(String productName,
                                String category,
                                Long price,
                                String description) {
}
