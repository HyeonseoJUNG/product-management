package com.example.coffeeserver.controller.dto.customer;

import com.example.coffeeserver.entity.customer.Customer;
import com.example.coffeeserver.entity.customer.Email;
import com.example.coffeeserver.entity.customer.PhoneNumber;

import java.time.LocalDateTime;
import java.util.UUID;

public class CustomersResponse {
    private final UUID customerId;
    private final String customerName;
    private final String email;
    private final String phoneNumber;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final LocalDateTime lastLoginAt;

    public CustomersResponse(UUID customerId, String customerName, Email email, PhoneNumber phoneNumber, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime lastLoginAt) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.email = email.getAddress();
        this.phoneNumber = phoneNumber.getPhoneNumber();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastLoginAt = lastLoginAt;
    }

    public CustomersResponse(Customer customer) {
        this.customerId = customer.getCustomerId();
        this.customerName = customer.getCustomerName();
        this.email = customer.getEmail().getAddress();
        this.phoneNumber = customer.getPhoneNumber().getPhoneNumber();
        this.createdAt = customer.getCreatedAt();
        this.updatedAt = customer.getUpdatedAt();
        this.lastLoginAt = customer.getLastLoginAt();
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }
}
