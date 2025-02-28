package com.example.security.controller;


import com.example.security.entity.Customer;
import com.example.security.repository.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerController(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> createCustomer(@RequestBody @Valid Customer customer) {
        try {

            String encodedPassword = passwordEncoder.encode(customer.getPassword());
            customer.setPassword(encodedPassword);
            Customer save = customerRepository.save(customer);
            if (save.getId() > 0) {
                return ResponseEntity.ok("User with email " + save.getEmail() + " saved successfully");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("User registration failed: "+e.getMessage());
        }

        return ResponseEntity.badRequest().body("Something went wrong");
    }
}
