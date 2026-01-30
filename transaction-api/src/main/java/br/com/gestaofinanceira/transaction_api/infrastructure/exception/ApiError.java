package br.com.gestaofinanceira.transaction_api.infrastructure.exception;

import java.time.Instant;

public record ApiError(
        int status,
        String error,
        String message,
        Instant timestamp
) {
    
}
