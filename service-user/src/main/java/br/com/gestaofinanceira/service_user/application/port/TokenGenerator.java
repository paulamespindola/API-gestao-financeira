package br.com.gestaofinanceira.service_user.application.port;

import br.com.gestaofinanceira.service_user.domain.model.Role;

import java.util.UUID;

public interface TokenGenerator {
    AuthResult generate(UUID userId, String email, Role role);
}
