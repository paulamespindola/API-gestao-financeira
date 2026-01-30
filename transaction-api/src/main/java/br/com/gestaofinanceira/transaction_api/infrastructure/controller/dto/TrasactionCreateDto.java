package br.com.gestaofinanceira.transaction_api.infrastructure.controller.dto;

import br.com.gestaofinanceira.transaction_api.domain.model.TransactionCategory;
import br.com.gestaofinanceira.transaction_api.domain.model.TransactionType;

public record TrasactionCreateDto(
        TransactionType type,
        TransactionCategory category,
        MoneyDto money,
        String destination,
        String description,
        boolean external
) {
}
