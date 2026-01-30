package br.com.gestaofinanceira.transaction_api.application;

import java.time.Instant;
import java.util.UUID;

public record TransactionDlqEvent(
        UUID transactionId,
        UUID userId,
        String errorType,
        String errorMessage,
        Instant occurredAt,
        Object payload
) {
}