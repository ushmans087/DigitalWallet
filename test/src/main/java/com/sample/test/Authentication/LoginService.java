package com.sample.test.Authentication;

import com.sample.test.DTO.LoginRequestDTO;
import com.sample.test.DTO.WalletRequestDTO;
import com.sample.test.Model.user;
import com.sample.test.Repository.LoginRepo;
import com.sample.test.Model.Role;
import com.sample.test.Util.JWTService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Service
public class LoginService {

    private final PasswordEncoder passwordEncoder;
    private final LoginRepo loginRepo;
    private final JWTService jwtService;
    private final RestClient.Builder restClientBuilder;

    public LoginService(
            PasswordEncoder passwordEncoder,
            LoginRepo loginRepo,
            JWTService jwtService,RestClient.Builder restClientBuilder) {

        this.passwordEncoder = passwordEncoder;
        this.loginRepo = loginRepo;
        this.jwtService = jwtService;
        this.restClientBuilder = restClientBuilder;
    }

    public String registerUser(LoginRequestDTO logUser) {

        String username = logUser.getUsername();
        String email = logUser.getEmail();
        String password = logUser.getPassword();

        var optionalUser = loginRepo.findByEmail(email);

        if (optionalUser.isPresent()) {
            return "User already registered";
        }

        String hashedPassword = passwordEncoder.encode(password);
        loginRepo.save(new user(username, hashedPassword, email));

        Optional<user> newUser = loginRepo.findByEmail(email);
        if(newUser.isEmpty())throw new RuntimeException("User not found");

        newUser.get().setRole(Role.USER);
        loginRepo.save(newUser.get());


        WalletRequestDTO walletRequestDTO = new WalletRequestDTO(newUser.get().getId(),username,email);
        restClientBuilder.build()
                .post()
                .uri("http://localhost:8086/wallet/create")
                .body(walletRequestDTO)
                .retrieve()
                .toBodilessEntity();

        return "Registered Successfully, please login";
    }

    public LoginResult login(LoginRequestDTO logUser) {

        String email = logUser.getEmail();
        String password = logUser.getPassword();

        var optionalUser = loginRepo.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return new LoginResult(false, "Email not registered", null);
        }

        if (!passwordEncoder.matches(password, optionalUser.get().getPassword())) {
            return new LoginResult(false, "Invalid Password", null);
        }

        String token = jwtService.generateToken(optionalUser.get().getId(),email,optionalUser.get().getRole());
        return new LoginResult(true, "Login Successful", token);
    }

    public static class LoginResult {

        private final boolean success;
        private final String message;
        private final String token;

        public LoginResult(
                boolean success,
                String message,
                String token) {

            this.success = success;
            this.message = message;
            this.token = token;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public String getToken() {
            return token;
        }
    }
}