package com.example.coffeeserver.controller;

import com.example.coffeeserver.controller.dto.product.ProductsResponse;
import com.example.coffeeserver.controller.dto.product.NewProductRequest;
import com.example.coffeeserver.controller.dto.product.UpdateProductRequest;
import com.example.coffeeserver.entity.product.Category;
import com.example.coffeeserver.entity.product.Product;
import com.example.coffeeserver.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/api/v1/product")
    public ResponseEntity createProduct(@RequestBody NewProductRequest request) {
        Product product = new Product(UUID.randomUUID(), request.productName(), Category.valueOf(request.category()), request.price(), request.description());
        productService.saveProduct(product);
        return new ResponseEntity<>("product was created successfully.", HttpStatus.CREATED);
    }

    @GetMapping("/api/v1/products")
    public ResponseEntity getProducts(@RequestParam("minPrice") Optional<Long> minPrice,
                                      @RequestParam("maxPrice") Optional<Long> maxPrice,
                                      @RequestParam("category") Optional<Category> category){
        List<Product> products = productService.getProductsByCondition(minPrice, maxPrice, category);
        List<ProductsResponse> responses = products.stream().map(ProductsResponse::new).collect(Collectors.toList());
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/api/v1/product/{productId}")
    public ResponseEntity getProduct(@PathVariable UUID productId) {
        Optional<Product> productById = productService.getProductById(productId);

        if (productById.isPresent()) {
            Product product = productById.get();
            ProductsResponse productsResponse = new ProductsResponse(product);
            return new ResponseEntity<>(productsResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>("There are no result.", HttpStatus.OK);
    }

    @PatchMapping("api/v1/product/{productId}")
    public ResponseEntity updateProduct(@PathVariable UUID productId, @RequestBody UpdateProductRequest request) {
        Optional<Product> productById = productService.getProductById(productId);
        if (productById.isEmpty()) {
            return new ResponseEntity<>("The update failed because the product does not exist.", HttpStatus.BAD_REQUEST);
        }
        Product product = productById.get();
        product.changeProductName(request.productName());
        product.changeProductCategory(Category.valueOf(request.category()));
        product.changePrice(request.price());
        product.changeDescription(request.description());
        productService.updateProduct(product);

        return new ResponseEntity<>("product was updated successfully.", HttpStatus.OK);
    }

    @DeleteMapping("api/v1/product/{productId}")
    public ResponseEntity deleteProduct(@PathVariable UUID productId) {
        productService.deleteProductById(productId);

        return new ResponseEntity<>("product was deleted successfully.", HttpStatus.OK);
    }
}
