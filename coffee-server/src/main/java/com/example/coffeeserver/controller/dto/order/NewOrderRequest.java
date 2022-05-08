package com.example.coffeeserver.controller.dto.order;

import com.example.coffeeserver.entity.order.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class NewOrderRequest {
    private final String address;
    private final List<OrderItemRequest> orderItemList;

    public NewOrderRequest(String address, List<OrderItemRequest> orderItemList) {
        this.address = address;
        this.orderItemList = orderItemList;
    }

    public String getAddress() {
        return address;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList.stream().map(OrderItemRequest::toEntity).collect(Collectors.toList());
    }
}
