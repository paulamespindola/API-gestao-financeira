package br.com.gestaofinanceira.transaction_api.application.usecase;

import br.com.gestaofinanceira.transaction_api.application.command.CreateTransactionCommand;
import br.com.gestaofinanceira.transaction_api.application.port.TransactionEventPublisher;
import br.com.gestaofinanceira.transaction_api.application.gateway.TransactionRepository;
import br.com.gestaofinanceira.transaction_api.domain.model.Transaction;

public record CreateTransactionUseCase(TransactionRepository repository, TransactionEventPublisher publisher){

    public Transaction execute(CreateTransactionCommand command){

        Transaction transaction = Transaction.create(command.userId(),
                command.type(),
                command.category(),
                command.amount(),
                command.destination(),
                command.description(),
                command.external());

        Transaction saved = repository.save(transaction);

        if(!saved.isExternal()){
            publisher.publishTransactionCreated(saved.getId().toString());
        }

        return saved;

    }

}
