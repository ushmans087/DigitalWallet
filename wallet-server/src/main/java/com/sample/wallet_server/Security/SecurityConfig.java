package com.sample.wallet_server.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String PUBLIC_KEY =
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA8XB9u6PmAfuZwIglqARP\n" +
                    "bl3LJu5rGcV42ozskHV1QjylHWcAOj5S55QN7nCL9qTkcTodUgLLkNWwIHKrqEFh\n" +
                    "C5Ym2DweyYZ6YNytOqYxdp8O/NpPNrc5xWBt2hn7mObPX0BlOmtd8Hnlc6EPmVsV\n" +
                    "bg86H6sdbdXbcvcVsL8flPKo5VHjfyArcwaM5I/a5BJ8KqrIMYhkFqY3ZKxzUrdS\n" +
                    "/NTynGfey+w8Lu8mhsFW0djJYT0PFcaL8XfC26ZBnRM24X83PZhm+JN0SE0tIimv\n" +
                    "i9Ck6l4xyN6yB2E1i2V0R1uARTCH41HAh1oRLxr/qx0vEcUkgzEh17/Y43ClDpVh\n" +
                    "JQIDAQAB";


    private RSAPublicKey getPublicKey() throws Exception {
        String publicKeyPEM = PUBLIC_KEY.replaceAll("\\s", "");
        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }

    @Bean
    public JwtDecoder jwtDecoder() throws Exception {
        return NimbusJwtDecoder.withPublicKey(getPublicKey()).build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http.csrf(csrf -> csrf.disable())
                .headers(headers -> headers
                        .frameOptions(frame -> frame.sameOrigin())
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/wallet/create",
                                "/wallet/findby/**",
                                "/wallet/update/balance",
                                "/h2-console/**"
                        ).permitAll()
                        .requestMatchers("/wallet/validator/**").hasAnyRole("TRANSACTION_VALIDATOR", "VALIDATOR")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {

            String role = jwt.getClaimAsString("role");
            return List.of(new SimpleGrantedAuthority("ROLE_" + role));
        });

        return converter;
    }
}