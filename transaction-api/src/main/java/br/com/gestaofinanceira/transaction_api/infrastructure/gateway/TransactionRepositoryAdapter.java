package br.com.gestaofinanceira.transaction_api.infrastructure.gateway;

import br.com.gestaofinanceira.transaction_api.application.gateway.TransactionRepository;
import br.com.gestaofinanceira.transaction_api.domain.model.Transaction;
import br.com.gestaofinanceira.transaction_api.domain.model.TransactionStatus;
import br.com.gestaofinanceira.transaction_api.infrastructure.persistence.TransactionEntity;
import br.com.gestaofinanceira.transaction_api.infrastructure.persistence.TransactionRepositoryJpa;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TransactionRepositoryAdapter implements TransactionRepository {

    private final TransactionRepositoryJpa repository;
    private final TransactionMapper mapper;

    public TransactionRepositoryAdapter(TransactionRepositoryJpa repository, TransactionMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Transaction save(Transaction transaction) {
        TransactionEntity entity = mapper.toEntity(transaction);
        repository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public Optional<Transaction> findById(UUID id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Transaction> findAllByUserIdAndDeletedFalse(UUID userId) {
        return repository.findAllByUserIdAndDeletedFalse(userId).
                stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Transaction> findByUserIdAndPeriod(UUID userId, Instant startDate, Instant endDate) {
        return repository.findByUserIdAndPeriod(userId, startDate, endDate, TransactionStatus.APPROVED)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
