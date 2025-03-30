package com.example.security.controller;


import com.example.security.entity.Customer;
import com.example.security.repository.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;

    }

    @GetMapping("/{email}")
    public ResponseEntity<Optional<Customer>> getCustomer(@PathVariable("email") String  email) {
        return ResponseEntity.ok()
                .body(customerRepository.findByEmail(email));
    }
}
