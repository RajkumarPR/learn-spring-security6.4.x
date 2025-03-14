package com.example.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ErrorController {

    @GetMapping("/login")
    private Model error(@RequestParam(value = "error", required = false) String error,
                         @RequestParam(value = "logout", required = false) String logout, Model model
    ) {
        if (error!=null && error.equals("true")) {
            return model.addAttribute("msg", "Invalid username or password");
        }
        if (logout!=null && logout.equals("true")) {
            return model.addAttribute("msg", "You have been logged out successfully");
        }
        return model.addAttribute("msg", "success");
    }
}
