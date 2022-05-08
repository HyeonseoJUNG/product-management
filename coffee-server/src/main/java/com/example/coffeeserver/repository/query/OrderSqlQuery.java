package com.example.coffeeserver.repository.query;

public enum OrderSqlQuery {
    SELECT_ALL("SELECT * FROM orders"),
    SELECT_BY_ID("SELECT * FROM orders WHERE order_id = UUID_TO_BIN(:orderId)"),

    INSERT_ORDER("INSERT INTO orders(order_id, customer_id, address, order_status, created_at, updated_at) " +
            "VALUES (UUID_TO_BIN(:orderId), UUID_TO_BIN(:customerId), :address, :orderStatus, :createdAt, :updatedAt)"),
    INSERT_ORDER_ITEM("INSERT INTO order_items(order_id, product_id, category, price, quantity, created_at) " +
            "VALUES (UUID_TO_BIN(:orderId), UUID_TO_BIN(:productId), :category, :price, :quantity, :createdAt)"),

    DELETE_ORDER_BY_ID("DELETE FROM orders WHERE order_id = UUID_TO_BIN(:orderId)"),

    DELETE_ALL("DELETE FROM orders");

    private final String query;

    OrderSqlQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
