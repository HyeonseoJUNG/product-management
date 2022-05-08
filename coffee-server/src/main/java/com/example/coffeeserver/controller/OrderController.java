package com.example.coffeeserver.controller;

import com.example.coffeeserver.controller.dto.order.NewOrderRequest;
import com.example.coffeeserver.controller.dto.order.OrdersResponse;
import com.example.coffeeserver.entity.order.Order;
import com.example.coffeeserver.entity.order.OrderStatus;
import com.example.coffeeserver.service.OrderService;
import com.example.coffeeserver.vo.OrderVo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
public class OrderController {
    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/v1/order/{customerId}")
    public ResponseEntity createOrder(@PathVariable UUID customerId, @RequestBody NewOrderRequest request) {
        OrderVo order = new OrderVo(UUID.randomUUID(), customerId, request.getAddress(), request.getOrderItemList(), OrderStatus.PAYMENT_CONFIRMED, LocalDateTime.now(), null);
        orderService.saveOrder(order);
        return new ResponseEntity<>("order was created successfully.", HttpStatus.CREATED);
    }

    @GetMapping("/api/v1/order/{orderId}")
    public ResponseEntity getOrder(@PathVariable UUID orderId) {
        Optional<Order> orderById = orderService.getOrderById(orderId);

        if (orderById.isPresent()) {
            Order order = orderById.get();
            OrdersResponse ordersResponse = new OrdersResponse(order);
            return new ResponseEntity<>(ordersResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>("There are no result.", HttpStatus.OK);
    }

    @DeleteMapping("api/v1/order/{orderId}")
    public ResponseEntity deleteOrder(@PathVariable UUID orderId) {
        orderService.deleteOrderById(orderId);

        return new ResponseEntity<>("order was deleted successfully.", HttpStatus.OK);
    }
}
