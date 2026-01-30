package br.com.gestaofinanceira.transaction_processor.application.gateway;

import br.com.gestaofinanceira.transaction_processor.infrastructure.persistence.TransactionEntity;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {

    Optional<TransactionEntity> findById(UUID id);

    TransactionEntity save(TransactionEntity transaction);
}
