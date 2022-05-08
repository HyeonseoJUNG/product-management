package com.example.coffeeserver.service;

import com.example.coffeeserver.entity.order.Order;
import com.example.coffeeserver.repository.OrderJdbcRepository;
import com.example.coffeeserver.vo.OrderVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {
    private OrderJdbcRepository orderRepository;

    public OrderService(OrderJdbcRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    /**
     * DB에 주문 정보 저장
     * @param order
     */
    public void saveOrder(OrderVo order) {
        orderRepository.insert(order);
    }

    /**
     * 주문 정보 id로 주문 정보 조회
     * @param orderId
     * @return
     */
    public Optional<Order> getOrderById(UUID orderId) {
        return orderRepository.findById(orderId);
    }

    /**
     * 주문 정보 id로 주문 정보 삭제
     * @param orderId
     */
    public void deleteOrderById(UUID orderId) {
        orderRepository.deleteById(orderId);
    }

}
