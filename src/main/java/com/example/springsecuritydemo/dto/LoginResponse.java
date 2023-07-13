package com.example.springsecuritydemo.dto;

import com.example.springsecuritydemo.entity.User;

// constructeur vide, constructeur "plein", getter et setter
public record LoginResponse(String token, User user) {
}