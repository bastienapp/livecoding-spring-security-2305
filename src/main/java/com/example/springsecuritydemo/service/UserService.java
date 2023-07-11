package com.example.springsecuritydemo.service;

import com.example.springsecuritydemo.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // TODO: charger le repository de user

        // TODO: vérifier que l'utilisateur existe en base de données
        if (!username.equals("user@test.com")) {
            throw new UsernameNotFoundException("No user found with username " + username);
        }

        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        // TODO: la logique qui renvoie l'utilisateur par son email de la base de données
        User user = new User("user@test.com", passwordEncoder.encode("tacostacos"));

        return user;
    }
}
