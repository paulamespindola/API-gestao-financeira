package br.com.gestaofinanceira.transaction_api.infrastructure.controller.dto;

import br.com.gestaofinanceira.transaction_api.domain.model.*;

public record TransactionResponseDto(
        TransactionStatus status,
        TransactionType type,
        TransactionCategory category,
        Money amount,
        String destination,
        String description,
        boolean external

) {

    public TransactionResponseDto(Transaction t){
        this(t.getStatus(), t.getType(), t.getCategory(),
                t.getOriginalAmount(), t.getDestination(), t.getDescription(), t.isExternal());
    }
}
