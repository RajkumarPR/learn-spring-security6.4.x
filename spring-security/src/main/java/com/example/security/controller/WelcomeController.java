package com.example.security.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {


    @GetMapping("/")
    public String welcome() {
        return "Welcome to spring security 6.4.x";
    }
}
