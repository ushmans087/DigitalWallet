package com.sample.test.Util;

import com.sample.test.Model.Role;
import com.sample.test.Model.User;
import com.sample.test.Repository.LoginRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initialize(LoginRepo userRepository, PasswordEncoder passwordEncoder) {

        return args -> {
            if (!userRepository.existsByUsername("validator")) {
                User validator = new User("validator", passwordEncoder.encode("validator123"), "validator@wallet.com");
                validator.setRole(Role.TRANSACTION_VALIDATOR);
                userRepository.save(validator);
            }
        };
    }
}