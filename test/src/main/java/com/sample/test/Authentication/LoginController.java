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
    public ResponseEntity<?> registerUser(@RequestBody LoginRequestDTO logUser) {
        loginService.registerUser(logUser);
        return new ResponseEntity<>("Account Created", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO logUser) {
        String result = loginService.login(logUser);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}