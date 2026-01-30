package br.com.gestaofinanceira.service_user.application.port;

import java.time.Instant;

public record AuthResult(String token, Instant dataExpiracao) {
}
