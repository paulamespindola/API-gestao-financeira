package br.com.gestaofinanceira.service_user.application.port;

import java.util.Optional;
import java.util.UUID;

public interface TokenVerifier {
    Optional<UUID> extractUserId(String token);

    boolean isValid(String token);
}
