package com.example.springsecuritydemo;

import com.example.springsecuritydemo.entity.Role;
import com.example.springsecuritydemo.entity.User;
import com.example.springsecuritydemo.repository.RoleRepository;
import com.example.springsecuritydemo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

@SpringBootApplication
public class SpringSecurityDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityDemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(
            RoleRepository roleRepository,
            UserRepository userRepository
    ) {

        return (args) -> {
            // manually creates roles on startup
            Optional<Role> adminRole = roleRepository.findByName("ROLE_ADMIN");
            Optional<Role> userRole = roleRepository.findByName("ROLE_USER");
            if (adminRole.isEmpty()) {
                roleRepository.save(new Role("ROLE_ADMIN"));
            }
            if (userRole.isEmpty()) {
                roleRepository.save(new Role("ROLE_USER"));
            }
            if (
                    adminRole.isPresent()
                            && userRole.isPresent()
                            && userRepository.findByEmail("admin@email.com").isEmpty()
            ) {
                PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
                User admin = new User("admin@email.com", passwordEncoder.encode("password"));
                admin.setRoles(Set.of(adminRole.get(), userRole.get()));
                userRepository.save(admin);
            }
        };
    }
}
