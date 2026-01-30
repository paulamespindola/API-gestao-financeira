package br.com.gestaofinanceira.transaction_api.application.port;

import java.util.Optional;
import java.util.UUID;

public interface TokenVerifier {
    Optional<UUID> extractUserId(String token);

    boolean isValid(String token);
}
