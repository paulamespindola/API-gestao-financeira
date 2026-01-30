package br.com.gestaofinanceira.transaction_processor.application.usecase;

import br.com.gestaofinanceira.transaction_processor.application.dto.AccountData;
import br.com.gestaofinanceira.transaction_processor.application.gateway.*;
import br.com.gestaofinanceira.transaction_processor.infrastructure.persistence.TransactionEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.UUID;

@Service
public class ProcessTransactionService {

    private final TransactionRepository repository;
    private final AccountClient accountClient;
    private final List<TransactionProcessor> processors;

    public ProcessTransactionService(
            TransactionRepository repository,
            AccountClient accountClient,
            List<TransactionProcessor> processors
    ) {
        this.repository = repository;
        this.accountClient = accountClient;
        this.processors = processors;
    }

    @Transactional
    public void process(UUID transactionId) {

        TransactionEntity transaction = repository.findById(transactionId)
                .orElseThrow();

        if (!transaction.isPending()) return;

        Long userId = (long) (Math.random() * 100);

        AccountData account = accountClient.getAccount(userId);

        TransactionProcessor processor = processors.stream()
                .filter(p -> p.supports(transaction.getType()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No processor for type"));

        processor.process(transaction, account);

        repository.save(transaction);
    }

}
