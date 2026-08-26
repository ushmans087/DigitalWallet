package com.sample.test.Util;

import com.sample.test.Repository.LoginRepo;
import com.sample.test.Model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class JWTService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final LoginRepo  loginRepo;

    @Autowired
    public JWTService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, LoginRepo  loginRepo) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.loginRepo = loginRepo;
    }

    public String generateToken(Long id, String email, Role role) {

        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(email)
                .issuedAt(now)
                .claim("id", id)
                .claim("role", role.name())
                .expiresAt(now.plusSeconds(3600))
                .build();

        return jwtEncoder
                .encode(JwtEncoderParameters.from(claims))
                .getTokenValue();
    }
    public boolean validateToken(String token) {
        try{
            Jwt j = jwtDecoder.decode(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
