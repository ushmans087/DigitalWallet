package com.sample.api_gateway.JWTUtil;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Configuration
public class JWTConfig {

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
    public ReactiveJwtDecoder jwtDecoder() throws Exception {
        return NimbusReactiveJwtDecoder.withPublicKey(getPublicKey()).build();
    }
}
