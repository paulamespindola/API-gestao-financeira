package br.com.gestaofinanceira.transaction_api.application.usecase;

import br.com.gestaofinanceira.transaction_api.application.gateway.TransactionRepository;
import br.com.gestaofinanceira.transaction_api.domain.model.Transaction;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class GenerateTransactionReportUseCase {

    private final TransactionRepository repository;

    public GenerateTransactionReportUseCase(TransactionRepository repository) {
        this.repository = repository;
    }

    public List<Transaction> execute(
            UUID userId,
            Instant startDate,
            Instant endDate
    ) {
        return repository.findByUserIdAndPeriod(userId, startDate, endDate);
    }
}
