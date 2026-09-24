package com.sample.test.Security;

import com.sample.test.Model.Role;
import com.sample.test.Model.User;
import com.sample.test.Repository.LoginRepo;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ValidatorConfig {

    private final LoginRepo loginRepo;
    private final PasswordEncoder passwordEncoder;

    public ValidatorConfig(LoginRepo loginRepo, PasswordEncoder passwordEncoder) {
        this.loginRepo = loginRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init(){
        if(loginRepo.existsByRole(Role.TRANSACTION_VALIDATOR))return;

        User validator = new User("validator",passwordEncoder.encode("validator123"),"validator@gmail.com");
        validator.setRole(Role.TRANSACTION_VALIDATOR);

        loginRepo.save(validator);
    }
}

