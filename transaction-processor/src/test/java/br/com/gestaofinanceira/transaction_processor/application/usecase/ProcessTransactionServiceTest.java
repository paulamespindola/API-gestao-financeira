package br.com.gestaofinanceira.transaction_processor.application.usecase;

import br.com.gestaofinanceira.transaction_processor.application.dto.AccountData;
import br.com.gestaofinanceira.transaction_processor.application.gateway.AccountClient;
import br.com.gestaofinanceira.transaction_processor.application.gateway.TransactionProcessor;
import br.com.gestaofinanceira.transaction_processor.application.gateway.TransactionRepository;
import br.com.gestaofinanceira.transaction_processor.infrastructure.persistence.TransactionEntity;
import br.com.gestaofinanceira.transaction_processor.infrastructure.persistence.TransactionStatus;
import br.com.gestaofinanceira.transaction_processor.infrastructure.persistence.TransactionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessTransactionServiceTest {

    @Mock
    TransactionRepository repository;

    @Mock
    AccountClient accountClient;

    @Mock
    TransactionProcessor processor;

    @InjectMocks
    ProcessTransactionService service;

    @Test
    void shouldDelegateToCorrectProcessor() {

        UUID id = UUID.randomUUID();

        TransactionEntity transaction = new TransactionEntity();
        transaction.setId(id);
        transaction.setType(TransactionType.PURCHASE);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setUserId(UUID.randomUUID());

        when(repository.findById(id)).thenReturn(Optional.of(transaction));
        when(accountClient.getAccount(1L)).thenReturn(mock(AccountData.class));
        when(processor.supports(TransactionType.PURCHASE)).thenReturn(true);

        service = new ProcessTransactionService(
                repository,
                accountClient,
                List.of(processor)
        );

        service.process(id);

        verify(processor).process(any(), any());
        verify(repository).save(transaction);
    }
}
