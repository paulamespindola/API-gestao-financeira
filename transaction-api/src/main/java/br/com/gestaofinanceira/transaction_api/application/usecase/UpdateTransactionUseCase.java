package br.com.gestaofinanceira.transaction_api.application.usecase;

import br.com.gestaofinanceira.transaction_api.application.command.UpdateTransactionCommand;
import br.com.gestaofinanceira.transaction_api.application.gateway.TransactionRepository;
import br.com.gestaofinanceira.transaction_api.domain.exception.DomainException;
import br.com.gestaofinanceira.transaction_api.domain.exception.TransactionNotFoundException;
import br.com.gestaofinanceira.transaction_api.domain.exception.UnauthorizedTransactionAccessException;
import br.com.gestaofinanceira.transaction_api.domain.model.Transaction;

import java.util.UUID;

public record UpdateTransactionUseCase(TransactionRepository repository) {

    public Transaction execute(UUID id, UpdateTransactionCommand command, UUID userId) {

        Transaction transaction = repository.findById(id)
                .orElseThrow(TransactionNotFoundException::new);

        if (!transaction.getUserId().equals(userId)) {
            throw new UnauthorizedTransactionAccessException();
        }

        transaction.update(
                command.type(),
                command.category(),
                command.destination(),
                command.description(),
                command.amount()
        );

        repository.save(transaction);

        return transaction;
    }
}
