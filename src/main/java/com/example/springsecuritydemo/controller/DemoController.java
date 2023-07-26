package com.example.springsecuritydemo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("")
    public String hello() {
        return "Hello world";
    }

    @GetMapping("/user")
    // @PreAuthorize("hasRole('ROLE_USER')") -> pour Basic Auth
    @PreAuthorize("hasAuthority('SCOPE_ROLE_USER')")
    public String userAccess() {
        return "User access";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    public String adminAccess() {
        return "Admin access";
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_ADMIN','SCOPE_ROLE_USER')")
    public String allAccess() {
        return "All access";
    }
}
