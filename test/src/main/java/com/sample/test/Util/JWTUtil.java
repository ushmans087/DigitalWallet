package com.sample.test.Util;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


@Configuration
public class JWTUtil {
    private final long expiration = 1000 * 60 * 15;

    private static final String PRIVATE_KEY =
            "MIIEugIBADANBgkqhkiG9w0BAQEFAASCBKQwggSgAgEAAoIBAQDxcH27o+YB+5nA\n" +
                    "iCWoBE9uXcsm7msZxXjajOyQdXVCPKUdZwA6PlLnlA3ucIv2pORxOh1SAsuQ1bAg\n" +
                    "cquoQWELlibYPB7Jhnpg3K06pjF2nw782k82tznFYG3aGfuY5s9fQGU6a13weeVz\n" +
                    "oQ+ZWxVuDzofqx1t1dty9xWwvx+U8qjlUeN/ICtzBozkj9rkEnwqqsgxiGQWpjdk\n" +
                    "rHNSt1L81PKcZ97L7Dwu7yaGwVbR2MlhPQ8Vxovxd8LbpkGdEzbhfzc9mGb4k3RI\n" +
                    "TS0iKa+L0KTqXjHI3rIHYTWLZXRHW4BFMIfjUcCHWhEvGv+rHS8RxSSDMSHXv9jj\n" +
                    "cKUOlWElAgMBAAECggEALJNgB1l4gQO2jJMmXqmjD6Wd+QtaNzkb+b+8emRROKAx\n" +
                    "POc0Jcy1aj+NvejGEoy5h4Ua/jX5Zv1o5ei99BPUCrtXYyNgbMUZCZKcogAmkDfI\n" +
                    "INtediPodN47keGrr7xna3bcaaeCgcBDonBJTL9CCt6hVND3Hzz1Zfmj5cy3lDxI\n" +
                    "LkVx2m4LhoBPM8PQDH0BuQyvWJyvbiF4W61VrESimrWJKkQlmT2ByNZk8l3fkMJw\n" +
                    "HgsVUUkjUXT9qKQCYqeqSxDumNRE9T1JBYtqNhIS85Ath7Dm4bXihuPwahL4PQ7d\n" +
                    "IdnihvmluJ0x6xqpkgG0p2VWxJ9dVhO29/ufV131EQKBgQD8SCXxVNjC4Qcrvvry\n" +
                    "2Zs08XIo8uSeNpyfqRrVYOoxYbsxQw8YgZuh/wlg5PIx86JXC4aEbOF+8lOQFkIj\n" +
                    "UXK9IdPxxjtlUgK9UpVO/Ha2mZSlp+L6Z7MDSj49QUnfEXK614Ra4nJDJoSJxoca\n" +
                    "bw7eAuhqfT2lMxAWshg94sedtQKBgQD0/29RuxPVM8rMg5mN7h/UeDQffVmL4Dgr\n" +
                    "zNGS6KON5J6spTSejGMq3Xg2cClkgcEm+kWEIVGTZbzrx5qRB2M/OkGBzUPzLx2e\n" +
                    "LBQRCrZHIByQ02f0yhReCHv9Hk8YO51ES2m5CCBWqOy0zQFUJ4A69Pno73lK94Gh\n" +
                    "COqx2LpbsQKBgA2giAwlHSBJr215f2ymvG97q0Ly+85f/M3TAJQODBhmwwnCKYqV\n" +
                    "VabGc+c79qkkE9zHzphQmU2WIVVjHmdx+tSkgyJIJuUuLN+1GkON94udeB4Kl4QX\n" +
                    "HeBLnlkIscjO1HerNIcEPE/MQNSAWMPgw9jSJ2O5KB9IRtIzBIQzP70RAoGAOY0j\n" +
                    "djyO/R9MfVxQnvGYEo4mGFuWxG/oYgmcFXmGqhdRWtb4FGKADRq3WXphjtWYLPLP\n" +
                    "j6lQA2L7zRs3aOgmXTzvFHHGi4K+32AVWqd8iBq3+yFeIzexLyGpFp2fEN84I0nf\n" +
                    "zPJmvlSyPgqGTGF3EuJnLlvKz1xy3AQ/NYXmyRECf0Hk5chrdepVNsk9FT3NV014\n" +
                    "jDVn0/czHi3rPWpVfvUuKmDrBB+y0a5MdKjivERSBZwyR/dYzGKG73X24sGmd9FY\n" +
                    "++7d4rRaCEHfXgm7JiiZTeFKXjcP0odk/fIVamsjglzmyzBVp/wu96RtCfGa0VHY\n" +
                    "1riMcBjDGNJYiGLAtGo=";

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

    private RSAPrivateKey getPrivateKey() throws Exception {
        String privateKeyPEM = PRIVATE_KEY.replaceAll("\\s", "");
        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    }

    @Bean
    public JwtDecoder jwtDecoder() throws Exception {
        return NimbusJwtDecoder.withPublicKey(getPublicKey()).build();
    }

    @Bean
    public JwtEncoder jwtEncoder() throws Exception {
        RSAKey jwk = new RSAKey.Builder(getPublicKey()).privateKey(getPrivateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }
}
