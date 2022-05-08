package com.example.coffeeserver.service;

import com.example.coffeeserver.entity.customer.Customer;
import com.example.coffeeserver.entity.customer.Email;
import com.example.coffeeserver.entity.customer.PhoneNumber;
import com.example.coffeeserver.repository.CustomerJdbcRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {
    private CustomerJdbcRepository customerRepository;

    public CustomerService(CustomerJdbcRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * DB에 고객 저장
     * @param customer
     */
    public void saveCustomer(Customer customer) {
        customerRepository.insert(customer);
    }

    /**
     * 모든 고객 리스트 반환
     * @return
     */
    public List<Customer> getAllCustomer() {
        return customerRepository.findAll();
    }

    /**
     * 고객 id로 고객 정보 조회
     * @param customerId
     * @return
     */
    public Optional<Customer> getCustomerById(UUID customerId) {
        return customerRepository.findById(customerId);
    }

    /**
     * 고객 이름으로 고객 정보 조회
     * @param name
     * @return
     */
    public List<Customer> getCustomerByName(String name) {
        return customerRepository.findByName(name);
    }

    /**
     * 고객 이메일로 고객 정보 조회
     * @param email
     * @return
     */
    public Optional<Customer> getCustomerByEmail(Email email) {
        return customerRepository.findByEmail(email);
    }

    /**
     * 고객 폰번호로 고객 정보 조회
     * @param phoneNumber
     * @return
     */
    public Optional<Customer> getCustomerByPhoneNumber(PhoneNumber phoneNumber) {
        return customerRepository.findByPhoneNumber(phoneNumber);
    }

    /**
     * 고객 정보 업데이트
     * @param customer
     */
    public void updateCustomer(Customer customer) {
        customerRepository.update(customer);
    }

    /**
     * 고객 id로 고객 삭제
     * @param customerId
     */
    public void deleteCustomerById(UUID customerId) {
        customerRepository.deleteById(customerId);
    }
}
