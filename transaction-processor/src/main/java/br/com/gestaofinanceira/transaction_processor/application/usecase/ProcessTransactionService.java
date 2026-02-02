package br.com.gestaofinanceira.transaction_processor.application.usecase;

import br.com.gestaofinanceira.transaction_processor.application.dto.AccountData;
import br.com.gestaofinanceira.transaction_processor.application.gateway.AccountClient;
import br.com.gestaofinanceira.transaction_processor.application.gateway.TransactionProcessor;
import br.com.gestaofinanceira.transaction_processor.infrastructure.persistence.TransactionEntity;
import br.com.gestaofinanceira.transaction_processor.application.exception.BusinessException;
import br.com.gestaofinanceira.transaction_processor.infrastructure.persistence.TransactionRepositoryJpa;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ProcessTransactionService {

    private static final Logger log = LoggerFactory.getLogger(ProcessTransactionService.class);

    private final TransactionRepositoryJpa repository;
    private final AccountClient accountClient;
    private final List<TransactionProcessor> processors;

    public ProcessTransactionService(
            TransactionRepositoryJpa repository,
            AccountClient accountClient,
            List<TransactionProcessor> processors
    ) {
        this.repository = repository;
        this.accountClient = accountClient;
        this.processors = processors;
    }

    @Transactional
    public void process(UUID transactionId) {

        log.info("Iniciando processamento da transação {}", transactionId);

        TransactionEntity transaction = repository.findById(transactionId)
                .orElseThrow(() -> new BusinessException("Transaction not found: " + transactionId));

        if (!transaction.isPending()) {
            log.info("Transação {} já processada, status={}", transactionId, transaction.getStatus());
            return;
        }

        AccountData account;
        try {
            Long userId = (long) (Math.random() *100);
            account = accountClient.getAccount(userId);
            if (account == null || account.currency() == null) {
                throw new BusinessException("Conta ou moeda inválida para a transação " + transactionId);
            }
        } catch (BusinessException e) {
            transaction.reject(e.getMessage());
            repository.save(transaction);
            log.error("BusinessException: transação {} rejeitada - {}", transactionId, e.getMessage());
            throw e; // envia para DLQ
        } catch (Exception e) {
            log.error("Erro técnico ao obter conta da transação {}: {}", transactionId, e.getMessage(), e);
            throw e; // retry Kafka
        }

        TransactionProcessor processor = processors.stream()
                .filter(p -> p.supports(transaction.getType()))
                .findFirst()
                .orElseThrow(() -> {
                    transaction.reject("Processor não encontrado para tipo " + transaction.getType());
                    repository.save(transaction);
                    log.error("Processor não encontrado para transação {}", transactionId);
                    return new BusinessException("No processor for type " + transaction.getType());
                });


        try {
            processor.process(transaction, account);
            repository.save(transaction);
            log.info("Transação {} aprovada com sucesso", transactionId);
        } catch (BusinessException e) {
            transaction.reject(e.getMessage());
            repository.save(transaction);
            log.error("BusinessException: transação {} rejeitada - {}", transactionId, e.getMessage());
            throw e; 
        } catch (Exception e) {
            log.error("Erro técnico ao processar transação {}: {}", transactionId, e.getMessage(), e);
            throw e;
        }
    }
}
