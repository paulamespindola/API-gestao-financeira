package br.com.gestaofinanceira.transaction_api.infrastructure.security_starter;

import br.com.gestaofinanceira.transaction_api.application.port.TokenVerifier;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class JwtTokenVerifier implements TokenVerifier {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.issuer:service-user}")
    private String issuer;

    @Override
    public boolean isValid(String token) {
        try {
            JWT.require(Algorithm.HMAC256(secret))
                    .withIssuer(issuer)
                    .build()
                    .verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Optional<UUID> extractUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return Optional.of(UUID.fromString(jwt.getSubject()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public List<String> extractRoles(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("roles").asList(String.class);
    }
}
