package br.com.gestaofinanceira.service_user.infrastructure.security.jwt;

import com.auth0.jwt.exceptions.JWTCreationException;

public class TokenGenerationException extends RuntimeException {
    public TokenGenerationException(JWTCreationException e) {
        super(e);
    }
}
