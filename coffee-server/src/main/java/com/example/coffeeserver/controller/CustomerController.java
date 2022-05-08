package com.example.coffeeserver.controller;

import com.example.coffeeserver.controller.dto.customer.CustomersResponse;
import com.example.coffeeserver.controller.dto.customer.NewCustomerRequest;
import com.example.coffeeserver.controller.dto.customer.UpdateCustomerRequest;
import com.example.coffeeserver.entity.customer.Customer;
import com.example.coffeeserver.entity.customer.Email;
import com.example.coffeeserver.entity.customer.PhoneNumber;
import com.example.coffeeserver.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/api/v1/customer")
    public ResponseEntity createCustomer(@RequestBody NewCustomerRequest request) {
        Customer customer = new Customer(UUID.randomUUID(), request.name(), new Email(request.email()), new PhoneNumber(request.phoneNumber()));
        customerService.saveCustomer(customer);
        return new ResponseEntity<>("customer was created successfully.", HttpStatus.CREATED);
    }

    @GetMapping("/api/v1/customers")
    public ResponseEntity getCustomers(){
        List<Customer> customers = customerService.getAllCustomer();
        List<CustomersResponse> responses = customers.stream().map(CustomersResponse::new).collect(Collectors.toList());
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/api/v1/customer/{customerId}")
    public ResponseEntity getCustomer(@PathVariable UUID customerId) {
        Optional<Customer> customerById = customerService.getCustomerById(customerId);

        if (customerById.isPresent()) {
            Customer customer = customerById.get();
            CustomersResponse customersResponse = new CustomersResponse(customer);
            return new ResponseEntity<>(customersResponse, HttpStatus.OK);
        }
        return new ResponseEntity<>("There are no result.", HttpStatus.OK);
    }

    @PatchMapping("api/v1/customer/{customerId}")
    public ResponseEntity updateCustomer(@PathVariable UUID customerId, @RequestBody UpdateCustomerRequest request) {
        Optional<Customer> customerById = customerService.getCustomerById(customerId);
        if (customerById.isEmpty()) {
            return new ResponseEntity<>("The update failed because the customer does not exist.", HttpStatus.BAD_REQUEST);
        }
        Customer customer = customerById.get();
        customer.changeCustomerName(request.name());
        customer.changePhoneNumber(new PhoneNumber(request.phoneNumber()));
        customerService.updateCustomer(customer);

        return new ResponseEntity<>("customer was updated successfully.", HttpStatus.OK);
    }

    @DeleteMapping("api/v1/customer/{customerId}")
    public ResponseEntity deleteCustomer(@PathVariable UUID customerId) {
        customerService.deleteCustomerById(customerId);

        return new ResponseEntity<>("customer was deleted successfully.", HttpStatus.OK);
    }
}
