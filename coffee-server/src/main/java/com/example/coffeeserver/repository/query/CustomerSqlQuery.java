package com.example.coffeeserver.repository.query;

public enum CustomerSqlQuery {
    SELECT_ALL("SELECT * FROM customers"),
    SELECT_BY_ID("SELECT * FROM customers WHERE customer_id = UUID_TO_BIN(:customerId)"),
    SELECT_BY_NAME("SELECT * FROM customers WHERE customer_name = :customerName"),
    SELECT_BY_EMAIL("SELECT * FROM customers WHERE email = :email"),
    SELECT_BY_PHONE_NUMBER("SELECT * FROM customers WHERE phone_number = :phoneNumber"),
    INSERT("INSERT INTO customers(customer_id, customer_name, email, phone_number, created_at) VALUES (UUID_TO_BIN(:customerId), :customerName, :email, :phoneNumber, :createdAt)"),
    UPDATE("UPDATE customers SET customer_name = :customerName, email = :email, phone_number = :phoneNumber, updated_At = :updatedAt WHERE customer_id = UUID_TO_BIN(:customerId)"),
    DELETE_BY_ID("DELETE FROM customers WHERE customer_id = UUID_TO_BIN(:customerId)"),
    DELETE_ALL("DELETE FROM customers");

    private final String query;

    CustomerSqlQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
