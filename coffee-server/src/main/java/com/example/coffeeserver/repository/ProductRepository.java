package com.example.coffeeserver.repository;

import com.example.coffeeserver.entity.customer.Customer;
import com.example.coffeeserver.entity.customer.Email;
import com.example.coffeeserver.entity.customer.PhoneNumber;
import com.example.coffeeserver.entity.product.Category;
import com.example.coffeeserver.entity.product.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 상품 정보를 관리하는 레포지터리
 */
public interface ProductRepository {

    Product insert(Product product);

    List<Product> findAll();

    Optional<Product> findById(UUID productId);

    List<Product> findByName(String name);

    List<Product> findByCondition(Optional<Long> minPrice,
                                  Optional<Long> maxPrice,
                                  Optional<Category> category);

    Product update(Product product);

    void deleteById(UUID productId);

    void deleteAll();
}
