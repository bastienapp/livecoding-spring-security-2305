package com.example.springsecuritydemo;

import com.example.springsecuritydemo.entity.Role;
import com.example.springsecuritydemo.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringSecurityDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityDemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(
            RoleRepository roleRepository
    ) {

        return (args) -> {
            // créer un role user et un role admin s'ils n'existent pas
            if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
                Role adminRole = new Role("ROLE_ADMIN");
                roleRepository.save(adminRole);
            }
            if (roleRepository.findByName("ROLE_USER").isEmpty()) {
                Role userRole = new Role("ROLE_USER");
                roleRepository.save(userRole);
            }
        };
    }
}
