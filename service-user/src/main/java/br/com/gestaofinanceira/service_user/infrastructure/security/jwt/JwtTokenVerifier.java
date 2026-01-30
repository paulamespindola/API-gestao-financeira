package br.com.gestaofinanceira.service_user.infrastructure.security.jwt;

import br.com.gestaofinanceira.service_user.application.port.TokenVerifier;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JwtTokenVerifier implements TokenVerifier {

    private final String secret;
    private final String issuer;

    public JwtTokenVerifier(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.issuer:service-user}") String issuer
    ) {
        this.secret = secret;
        this.issuer = issuer;
    }

    @Override
    public Optional<UUID> extractUserId(String token) {
        try {
            DecodedJWT jwt = verify(token);
            return Optional.of(UUID.fromString(jwt.getSubject()));
        } catch (JWTVerificationException e) {
            return Optional.empty();
        }
    }


    @Override
    public boolean isValid(String token) {
        try {
            verify(token);
            return true;
        }catch (JWTVerificationException e){
            return false;
        }
    }

    private DecodedJWT verify(String token){
        Algorithm algorithm = Algorithm.HMAC256(secret);

        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();

        return verifier.verify(token);
    }

    public List<String> extractRoles(String token) {
        DecodedJWT jwt = JWT.decode(token);

        List<String> roles = jwt.getClaim("roles").asList(String.class);

        if (roles == null || roles.isEmpty()) {
            String scope = jwt.getClaim("scope").asString();
            if (scope != null) {
                roles = List.of(scope);
            }
        }

        return roles == null ? List.of() : roles;
    }

}
