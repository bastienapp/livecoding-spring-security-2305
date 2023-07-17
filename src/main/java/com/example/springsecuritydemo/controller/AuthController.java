package com.example.springsecuritydemo.controller;

import com.example.springsecuritydemo.dto.LoginResponse;
import com.example.springsecuritydemo.entity.Role;
import com.example.springsecuritydemo.entity.User;
import com.example.springsecuritydemo.repository.RoleRepository;
import com.example.springsecuritydemo.repository.UserRepository;
import com.example.springsecuritydemo.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authManager;
    private final TokenService tokenService;

    public AuthController(
            UserRepository userRepositoryInjected,
            RoleRepository roleRepositoryInjected,
            AuthenticationManager authManagerInjected,
            TokenService tokenServiceInjected
    ) {
        this.userRepository = userRepositoryInjected;
        this.roleRepository = roleRepositoryInjected;
        this.authManager = authManagerInjected;
        this.tokenService = tokenServiceInjected;
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

    // TODO: utiliser un DTO
    @PostMapping("/login")
    public LoginResponse login(@RequestBody User user) {

        // j'ai besoin de récupérer l'objet d'authentification de Spring pour cet utilisateur
        Authentication auth = this.authManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getEmail(), user.getPassword()
        ));
        String token = tokenService.generateToken(auth);
        // récupère l'utilisateur connecté
        User userConnected = (User) auth.getPrincipal();

        return new LoginResponse(token, userConnected);
    }
}
