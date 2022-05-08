package com.example.coffeeserver.repository;

import com.example.coffeeserver.entity.order.Order;
import com.example.coffeeserver.vo.OrderVo;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    OrderVo insert(OrderVo order);

    Optional<Order> findById(UUID orderId);

    void deleteById(UUID orderId);

    void deleteAll();
}
