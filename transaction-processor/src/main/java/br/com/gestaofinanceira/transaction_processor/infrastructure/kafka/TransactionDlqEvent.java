package br.com.gestaofinanceira.transaction_processor.infrastructure.kafka;

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