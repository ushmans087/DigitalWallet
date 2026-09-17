package com.sample.test.Authentication;

import com.sample.test.DTO.LoginRequestDTO;
import com.sample.test.DTO.UserRegisterDTO;
import com.sample.test.ExceptionClass.AccountAlreadyExistException;
import com.sample.test.ExceptionClass.AccountNotFoundException;
import com.sample.test.ExceptionClass.InvalidPasswordException;
import com.sample.test.Model.User;
import com.sample.test.Repository.LoginRepo;
import com.sample.test.Util.JWTService;
import com.sample.test.kafkaService.ProducerService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final PasswordEncoder passwordEncoder;
    private final LoginRepo loginRepo;
    private final JWTService jwtService;
    private final ProducerService producerService;

    public LoginService(PasswordEncoder passwordEncoder, LoginRepo loginRepo, JWTService jwtService,ProducerService producerService) {
        this.passwordEncoder = passwordEncoder;
        this.loginRepo = loginRepo;
        this.jwtService = jwtService;
        this.producerService = producerService;
    }

    @Transactional
    public String registerUser(LoginRequestDTO logUser) {

        String username = logUser.getUsername();
        String email = logUser.getEmail();
        String password = logUser.getPassword();

        var optionalUser = loginRepo.findByEmail(email);

        //Exp
        if (optionalUser.isPresent())throw new AccountAlreadyExistException("Account already exist");

        //hash the password and store
        String hashedPassword = passwordEncoder.encode(password);
        User newUser = loginRepo.save(new User(username, hashedPassword, email));

        // kafka event for wallet creation
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO(newUser.getId(),username,email);
        producerService.publishUserRegisterEvent(userRegisterDTO);

        return "Registered Successfully, please login";
    }

    public String login(LoginRequestDTO logUser) {

        String email = logUser.getEmail();
        String password = logUser.getPassword();

        var optionalUser = loginRepo.findByEmail(email);

        //Exp in case if no account or invalid password
        if (optionalUser.isEmpty())throw new AccountNotFoundException("Account not found");
        if (!passwordEncoder.matches(password, optionalUser.get().getPassword()))throw new InvalidPasswordException("Email or Password is invalid");

        String token = jwtService.generateToken(optionalUser.get().getId(),email,optionalUser.get().getRole());
        return token;
    }
}