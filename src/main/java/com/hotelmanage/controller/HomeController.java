package com.hotelmanage.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "users/homepage";
    }

    @GetMapping({"/admin", "/reception", "/home"})
    public String roleLanding(Authentication authentication) {
        if (authentication == null) return "index";
        for (GrantedAuthority a : authentication.getAuthorities()) {
            String role = a.getAuthority();
            if ("ROLE_ADMIN".equals(role)) return "admin/admin-dashboard";
            if ("ROLE_RECEPTIONIST".equals(role)) return "receptionist/receptionist-dashboard";
            if ("ROLE_CUSTOMER".equals(role)) return "users/homepage";
        }
        return "index";
    }
}

