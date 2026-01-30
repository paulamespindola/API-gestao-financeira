package br.com.gestaofinanceira.transaction_processor.infrastructure.gateway;

import br.com.gestaofinanceira.transaction_processor.application.gateway.TransactionRepository;

import br.com.gestaofinanceira.transaction_processor.infrastructure.persistence.TransactionEntity;
import br.com.gestaofinanceira.transaction_processor.infrastructure.persistence.TransactionRepositoryJpa;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class TransactionRepositoryAdapter implements TransactionRepository {

    private final TransactionRepositoryJpa jpa;

    public TransactionRepositoryAdapter(TransactionRepositoryJpa jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<TransactionEntity> findById(UUID id) {
        return jpa.findById(id);
    }

    @Override
    public TransactionEntity save(TransactionEntity transaction) {
        return jpa.save(transaction);
    }
}
