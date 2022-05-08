package com.example.coffeeserver.repository;

import com.example.coffeeserver.entity.customer.Customer;
import com.example.coffeeserver.entity.customer.Email;
import com.example.coffeeserver.entity.customer.PhoneNumber;
import com.example.coffeeserver.entity.order.Order;
import com.example.coffeeserver.entity.order.OrderItem;
import com.example.coffeeserver.entity.order.OrderStatus;
import com.example.coffeeserver.repository.query.CustomerSqlQuery;
import com.example.coffeeserver.repository.query.OrderSqlQuery;
import com.example.coffeeserver.repository.query.ProductSqlQuery;
import com.example.coffeeserver.utils.Util;
import com.example.coffeeserver.vo.OrderVo;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public class OrderJdbcRepository implements OrderRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public OrderJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Order> orderRowMapper = (resultSet, rowNum) -> {
        UUID orderId = Util.toUUID(resultSet.getBytes("order_id"));
        UUID customerId = Util.toUUID(resultSet.getBytes("customer_id"));
        String address = resultSet.getString("address");
        OrderStatus orderStatus = OrderStatus.valueOf(resultSet.getString("order_status"));
        LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        LocalDateTime updatedAt = resultSet.getTimestamp("updated_at") != null ?
                resultSet.getTimestamp("updated_at").toLocalDateTime() : null;

        return new Order(orderId, customerId, address, orderStatus, createdAt, updatedAt);
    };

    private Map<String, Object> toOrderParamMap(OrderVo order) {
        var paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", order.getOrderId().toString().getBytes());
        paramMap.put("customerId", order.getCustomerId().toString().getBytes());
        paramMap.put("address", order.getAddress());
        paramMap.put("orderStatus", order.getOrderStatus().toString());
        paramMap.put("createdAt", order.getCreatedAt());
        paramMap.put("updatedAt", order.getUpdatedAt());
        return paramMap;
    }

    private Map<String, Object> toOrderItemParamMap(UUID orderId, LocalDateTime createdAt, OrderItem item) {
        var paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", orderId.toString().getBytes());
        paramMap.put("productId", item.getProductId().toString().getBytes());
        paramMap.put("category", item.getCategory().toString());
        paramMap.put("price", item.getPrice());
        paramMap.put("quantity", item.getQuantity());
        paramMap.put("createdAt", createdAt);
        return paramMap;
    }

    @Override
    public OrderVo insert(OrderVo order) {
        jdbcTemplate.update(OrderSqlQuery.INSERT_ORDER.getQuery(),
                toOrderParamMap(order));
        order.getOrderItems()
                .forEach(item ->
                        jdbcTemplate.update(OrderSqlQuery.INSERT_ORDER_ITEM.getQuery(),
                                toOrderItemParamMap(order.getOrderId(), order.getCreatedAt(), item)));
        return order;
    }

    @Override
    public Optional<Order> findById(UUID orderId) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(OrderSqlQuery.SELECT_BY_ID.getQuery(),
                            Collections.singletonMap("orderId", orderId.toString().getBytes()),
                            orderRowMapper)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(UUID orderId) {
        jdbcTemplate.update(
                OrderSqlQuery.DELETE_ORDER_BY_ID.getQuery(),
                Collections.singletonMap("orderId", orderId.toString().getBytes()));
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update(OrderSqlQuery.DELETE_ALL.getQuery(), Collections.emptyMap());
    }


}
