package com.example.coffeeserver.controller.dto.order;

import com.example.coffeeserver.entity.order.Order;
import com.example.coffeeserver.entity.order.OrderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class OrdersResponse {
    private final UUID orderId;
    private final UUID customerId;
    private final String address;
    private final String orderStatus;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public OrdersResponse(UUID orderId, UUID customerId, String address, OrderStatus orderStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.address = address;
        this.orderStatus = orderStatus.name();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public OrdersResponse(Order order) {
        this.orderId = order.getOrderId();
        this.customerId = order.getCustomerId();
        this.address = order.getAddress();
        this.orderStatus = order.getOrderStatus().name();
        this.createdAt = order.getCreatedAt();
        this.updatedAt = order.getUpdatedAt();
    }

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public String getAddress() {
        return address;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
