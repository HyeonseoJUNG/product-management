package com.example.coffeeserver.repository.query;

public enum ProductSqlQuery {
    SELECT_ALL("SELECT * FROM products"),
    SELECT_BY_ID("SELECT * FROM products WHERE product_id = UUID_TO_BIN(:productId)"),
    SELECT_BY_NAME("SELECT * FROM products WHERE product_name = :productName"),
    INSERT("INSERT INTO products(product_id, product_name, category, price, description, created_at, updated_at)" +
            " VALUES(UUID_TO_BIN(:productId), :productName, :category, :price, :description, :createdAt, :updatedAt)"),
    UPDATE("UPDATE products SET product_name = :productName, category = :category, price = :price, description = :description, updated_At = :updatedAt WHERE product_id = UUID_TO_BIN(:productId)"),
    DELETE_BY_ID("DELETE FROM products WHERE product_id = UUID_TO_BIN(:productId)"),
    DELETE_ALL("DELETE FROM products");

    private final String query;

    ProductSqlQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
