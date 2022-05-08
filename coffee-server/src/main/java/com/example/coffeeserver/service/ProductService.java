package com.example.coffeeserver.service;

import com.example.coffeeserver.entity.product.Category;
import com.example.coffeeserver.entity.product.Product;
import com.example.coffeeserver.repository.ProductJdbcRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {
    private ProductJdbcRepository productRepository;

    public ProductService(ProductJdbcRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * DB에 상품 정보 저장
     * @param product
     */
    public void saveProduct(Product product) {
        productRepository.insert(product);
    }

    /**
     * 모든 상품 정보 리스트 반환
     * @return
     */
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    /**
     * 상품 정보 id로 상품 정보 조회
     * @param productId
     * @return
     */
    public Optional<Product> getProductById(UUID productId) {
        return productRepository.findById(productId);
    }

    /**
     * 상품 이름으로 상품 정보 정보 조회
     * @param name
     * @return
     */
    public List<Product> getProductByName(String name) {
        return productRepository.findByName(name);
    }

    /**
     * 조건(가격 범위, 카테고리)에 따라 상품 정보 조회
     * @param minPrice
     * @param maxPrice
     * @param category
     * @return
     */
    public List<Product> getProductsByCondition(Optional<Long> minPrice,
                                                   Optional<Long> maxPrice,
                                                   Optional<Category> category) {
        return productRepository.findByCondition(minPrice, maxPrice, category);
    }

    /**
     * 상품 정보 업데이트
     * @param product
     */
    public void updateProduct(Product product) {
        productRepository.update(product);
    }

    /**
     * 상품 정보 id로 상품 정보 삭제
     * @param productId
     */
    public void deleteProductById(UUID productId) {
        productRepository.deleteById(productId);
    }

}
