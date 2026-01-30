package br.com.gestaofinanceira.transaction_api.application.command;

import br.com.gestaofinanceira.transaction_api.domain.model.Money;
import br.com.gestaofinanceira.transaction_api.domain.model.TransactionCategory;
import br.com.gestaofinanceira.transaction_api.domain.model.TransactionType;

public record UpdateTransactionCommand(
        TransactionType type,
        TransactionCategory category,
        Money amount,
        String destination,
        String description
) {
}
