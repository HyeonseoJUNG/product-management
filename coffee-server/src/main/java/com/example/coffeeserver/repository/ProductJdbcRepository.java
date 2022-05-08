package com.example.coffeeserver.repository;

import com.example.coffeeserver.entity.product.Category;
import com.example.coffeeserver.entity.product.Product;
import com.example.coffeeserver.repository.query.ProductSqlQuery;
import com.example.coffeeserver.utils.Util;
import com.example.exception.DataNotInsertedException;
import com.example.exception.DataNotUpdatedException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class ProductJdbcRepository implements ProductRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final RowMapper<Product> productRowMapper = (resultSet, rowNum) -> {
        UUID productId = Util.toUUID(resultSet.getBytes("product_id"));
        String productName = resultSet.getString("product_name");
        Category category = Category.valueOf(resultSet.getString("category"));
        long price = resultSet.getLong("price");
        String description = resultSet.getString("description");
        LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
        LocalDateTime updatedAt = resultSet.getTimestamp("updated_at") != null ?
                resultSet.getTimestamp("updated_at").toLocalDateTime() : null;

        return new Product(productId, productName, category, price, description, createdAt, updatedAt);
    };

    private Map<String, Object> toParamMap(Product product) {
        return new HashMap<>(){{
            put("productId", product.getProductId().toString().getBytes());
            put("productName", product.getProductName());
            put("category", product.getCategory().toString());
            put("price", product.getPrice());
            put("description", product.getDescription());
            put("createdAt", Timestamp.valueOf(product.getCreatedAt()));
            put("updatedAt", product.getUpdatedAt() != null ? Timestamp.valueOf(product.getUpdatedAt()) : null);
        }};
    }

    public ProductJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Product insert(Product product) {
        int update = jdbcTemplate.update(ProductSqlQuery.INSERT.getQuery(), toParamMap(product));

        if (update != 1) {
            throw new DataNotInsertedException();
        }

        return product;
    }

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query(ProductSqlQuery.SELECT_ALL.getQuery(), productRowMapper);
    }

    @Override
    public Optional<Product> findById(UUID productId) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(ProductSqlQuery.SELECT_BY_ID.getQuery(),
                            Collections.singletonMap("productId", productId.toString().getBytes()),
                            productRowMapper)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Product> findByName(String name) {
        return jdbcTemplate.query(
                ProductSqlQuery.SELECT_BY_NAME.getQuery(),
                Collections.singletonMap("productName", name),
                productRowMapper);
    }

    @Override
    public List<Product> findByCondition(Optional<Long> minPrice, Optional<Long> maxPrice, Optional<Category> category) {
        int count = 0;
        Map paramMap = new HashMap<>();
        String query = "SELECT * FROM products";
        if (minPrice.isPresent() || maxPrice.isPresent() || category.isPresent()) {
            query += " WHERE";
        }
        if (minPrice.isPresent()) {
            query += " price >= :min";
            count++;
            paramMap.put("min", minPrice.get());
        }
        if (maxPrice.isPresent()) {
            if (count > 0) {
                query += " AND";
            }
            query += " price <= :max";
            count++;
            paramMap.put("max", maxPrice.get());
        }
        if (category.isPresent()) {
            if (count > 0) {
                query += " AND";
            }
            query += " category = :category";
            paramMap.put("category", category.get().name());
        }

        List list = jdbcTemplate.query(
                query,
                paramMap,
                productRowMapper);

        return list;
    }

    @Override
    public Product update(Product product) {
        int update = jdbcTemplate.update(ProductSqlQuery.UPDATE.getQuery(), toParamMap(product));

        if (update != 1) {
            throw new DataNotUpdatedException();
        }

        return product;
    }

    @Override
    public void deleteById(UUID productId) {
        jdbcTemplate.update(
                ProductSqlQuery.DELETE_BY_ID.getQuery(),
                Collections.singletonMap("productId", productId.toString().getBytes()));
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update(ProductSqlQuery.DELETE_ALL.getQuery(), Collections.emptyMap());

    }
}
