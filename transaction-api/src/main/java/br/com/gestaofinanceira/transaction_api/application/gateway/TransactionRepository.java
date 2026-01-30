package br.com.gestaofinanceira.transaction_api.application.gateway;

import br.com.gestaofinanceira.transaction_api.domain.model.Transaction;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface TransactionRepository {
   Transaction save(Transaction transaction);
   Optional<Transaction> findById(UUID id);
   List<Transaction> findAllByUserIdAndDeletedFalse(UUID userId);
   List<Transaction> findByUserIdAndPeriod(UUID userId, Instant startDate, Instant endDate);

}
