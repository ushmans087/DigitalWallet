package com.sample.test.Authentication;

import com.sample.test.DTO.LoginRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @RequestBody LoginRequestDTO logUser) {

        String result = loginService.registerUser(logUser);
        if (result.equals("User already registered")) {
            return new ResponseEntity<>(result, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO logUser) {

        LoginService.LoginResult result = loginService.login(logUser);
        if (!result.isSuccess()) {
            HttpStatus status = result.getMessage().equals("Email not registered") ? HttpStatus.NOT_FOUND : HttpStatus.UNAUTHORIZED;
            return new ResponseEntity<>(result.getMessage(), status);
        }
        return new ResponseEntity<>(result.getToken(), HttpStatus.OK);
    }

    @GetMapping("/profile")
    public String profile(@RequestBody LoginRequestDTO logUser) {
        return ":)";
    }
}