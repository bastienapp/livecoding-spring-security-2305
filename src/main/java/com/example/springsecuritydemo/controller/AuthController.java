package com.example.springsecuritydemo.controller;

import com.example.springsecuritydemo.entity.Role;
import com.example.springsecuritydemo.entity.User;
import com.example.springsecuritydemo.repository.RoleRepository;
import com.example.springsecuritydemo.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AuthController(
            UserRepository userRepositoryInjected,
            RoleRepository roleRepositoryInjected
    ) {
        this.userRepository = userRepositoryInjected;
        this.roleRepository = roleRepositoryInjected;
    }

    @PostMapping("/register")
    public User register(@RequestBody User newUser) {
        // si l'utilisateur existe déjà sur cet email
        if (this.userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            // lever une exception
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User already exists with email " + newUser.getEmail());
        }
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        // associer le role ROLE_USER à mon nouvel utilisateur
        Role userRole = this.roleRepository.findByName("ROLE_USER")
                        .orElseThrow(() ->
                                new ResponseStatusException(HttpStatus.NOT_FOUND,
                                        "No ROLE_USER found"));
        newUser.setRoles(Set.of(userRole));
        return this.userRepository.save(newUser);
    }
}
