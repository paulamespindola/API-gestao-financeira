package br.com.gestaofinanceira.transaction_api.application.usecase;

import br.com.gestaofinanceira.transaction_api.application.gateway.TransactionRepository;
import br.com.gestaofinanceira.transaction_api.domain.model.Transaction;

import java.util.List;
import java.util.UUID;

public record ListTransactionUseCase(TransactionRepository repository){
    public List<Transaction> execute(UUID userId){
        return repository.findAllByUserIdAndDeletedFalse(userId);
    }
}
