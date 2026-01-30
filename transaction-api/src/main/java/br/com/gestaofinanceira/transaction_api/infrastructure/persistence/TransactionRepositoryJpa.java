package br.com.gestaofinanceira.transaction_api.infrastructure.persistence;

import br.com.gestaofinanceira.transaction_api.domain.model.Transaction;
import br.com.gestaofinanceira.transaction_api.domain.model.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface TransactionRepositoryJpa extends JpaRepository<TransactionEntity, UUID> {
    List<TransactionEntity> findAllByUserIdAndDeletedFalse(UUID userId);

    @Query("""
    SELECT t FROM TransactionEntity t
    WHERE t.userId = :userId
      AND t.createdAt BETWEEN :start AND :end
      AND t.deleted = false
      AND t.status = :status
    """)
    List<TransactionEntity> findByUserIdAndPeriod(
            UUID userId,
            Instant start,
            Instant end,
            TransactionStatus status
    );

}
