package com.example.coffeeserver.vo;

import com.example.coffeeserver.entity.order.Order;
import com.example.coffeeserver.entity.order.OrderItem;
import com.example.coffeeserver.entity.order.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class OrderVo {
    private final UUID orderId;
    private final UUID customerId;
    private final String address;
    private final List<OrderItem> orderItems;
    private final OrderStatus orderStatus;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public OrderVo(UUID orderId, UUID customerId, String address, List<OrderItem> orderItems, OrderStatus orderStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.address = address;
        this.orderItems = orderItems;
        this.orderStatus = orderStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Order toOrderEntity() {
        return new Order(orderId, customerId, address, orderStatus, createdAt, updatedAt);
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

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderVo)) return false;
        OrderVo orderVo = (OrderVo) o;
        return Objects.equals(getOrderId(), orderVo.getOrderId()) && Objects.equals(getCustomerId(), orderVo.getCustomerId()) && Objects.equals(getAddress(), orderVo.getAddress()) && Objects.equals(getOrderItems(), orderVo.getOrderItems()) && getOrderStatus() == orderVo.getOrderStatus() && Objects.equals(getCreatedAt(), orderVo.getCreatedAt()) && Objects.equals(getUpdatedAt(), orderVo.getUpdatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderId(), getCustomerId(), getAddress(), getOrderItems(), getOrderStatus(), getCreatedAt(), getUpdatedAt());
    }
}
