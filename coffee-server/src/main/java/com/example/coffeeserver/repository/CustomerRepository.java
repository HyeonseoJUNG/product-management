package com.example.coffeeserver.repository;

import com.example.coffeeserver.entity.customer.Customer;
import com.example.coffeeserver.entity.customer.Email;
import com.example.coffeeserver.entity.customer.PhoneNumber;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 고객 데이터를 관리하는 레포지포리
 */
public interface CustomerRepository {

    Customer insert(Customer customer);

    List<Customer> findAll();

    Optional<Customer> findById(UUID customerId);

    List<Customer> findByName(String name);

    Optional<Customer> findByEmail(Email email);

    Optional<Customer> findByPhoneNumber(PhoneNumber phoneNumber);

    Customer update(Customer customer);

    void deleteById(UUID customerId);

    void deleteAll();

}
