package br.com.gestaofinanceira.service_user.infrastructure.security.jwt;

import br.com.gestaofinanceira.service_user.application.port.AuthResult;
import br.com.gestaofinanceira.service_user.application.port.TokenGenerator;
import br.com.gestaofinanceira.service_user.domain.model.Role;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;

import java.util.List;
import java.util.UUID;

public class JwtTokenGenerator implements TokenGenerator {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.issuer:service-user}")
    private String issuer;

    @Override
    public AuthResult generate(UUID userPublicId, String email, Role role) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            Instant expiresAt = expirationInstant();

            String token = JWT.create()
                    .withIssuer(issuer)
                    .withSubject(userPublicId.toString())
                    .withClaim("email", email)
                    .withClaim("scope", role.name())
                    .withClaim("roles", List.of(role.name()))
                    .withExpiresAt(expiresAt)
                    .withIssuedAt(Instant.now())
                    .sign(algorithm);

            return new AuthResult(token, expiresAt);

        } catch (JWTCreationException e) {
            throw new TokenGenerationException(e);
        }
    }

    private Instant expirationInstant() {
        return Instant.now().plusSeconds(60 * 60 * 2);
    }
}
