package br.com.gestaofinanceira.service_user.infrastructure.security;

import br.com.gestaofinanceira.service_user.application.port.PasswordMatcher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordMatcher implements PasswordMatcher {

    private final PasswordEncoder encoder;

    public BCryptPasswordMatcher(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public boolean matches(String raw, String encoded) {
        return encoder.matches(raw, encoded);
    }
}
