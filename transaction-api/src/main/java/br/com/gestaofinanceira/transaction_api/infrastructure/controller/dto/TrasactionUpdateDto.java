package br.com.gestaofinanceira.transaction_api.infrastructure.controller.dto;

import br.com.gestaofinanceira.transaction_api.domain.model.Money;
import br.com.gestaofinanceira.transaction_api.domain.model.TransactionCategory;
import br.com.gestaofinanceira.transaction_api.domain.model.TransactionType;

public record TrasactionUpdateDto(
        TransactionType type,
        TransactionCategory category,
        Money money,
        String destination,
        String description
) {
}
