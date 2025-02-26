package com.example.security.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {


    @GetMapping("/")
    public String welcome() {
        return "Welcome to spring security 6.4.x";
    }
    @GetMapping("/welcome")
    public String welcome2() {
        return "Testing welcome Unsecured endpoint";
    }
    @GetMapping("/secured")
    public String secured() {
        return "Secured with spring security 6.4.x";
    }
}
