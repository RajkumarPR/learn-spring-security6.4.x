package com.example.security.service;

import com.example.security.entity.Customer;
import com.example.security.repository.CustomerRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Application logic to get user from database
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with given email: "+email));

        // prepare the authorities attached to the user
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(customer.getRole()));

        // return the user object as org.springframework.security.core.userdetails.User implements the UserDetails
        // from here spring security framework kicks in does the authentication
        return new User(customer.getEmail(), customer.getPassword(), authorities);
    }
}
