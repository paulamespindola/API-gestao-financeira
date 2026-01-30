package br.com.gestaofinanceira.transaction_processor.infrastructure.dto;

import java.util.UUID;

public record TransactionRequestedEvent(
        UUID transactionId
) {}
