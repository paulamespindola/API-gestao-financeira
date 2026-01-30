package br.com.gestaofinanceira.transaction_processor.application.usecase;

import br.com.gestaofinanceira.transaction_processor.application.dto.AccountData;
import br.com.gestaofinanceira.transaction_processor.application.gateway.AccountClient;
import br.com.gestaofinanceira.transaction_processor.infrastructure.persistence.TransactionEntity;
import br.com.gestaofinanceira.transaction_processor.infrastructure.persistence.TransactionStatus;
import br.com.gestaofinanceira.transaction_processor.infrastructure.persistence.TransactionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DepositTransactionProcessorTest {

    @Mock
    AccountClient accountClient;

    @InjectMocks
    DepositTransactionProcessor processor;

    @Test
    void shouldApproveDeposit() {

        TransactionEntity transaction = new TransactionEntity();
        transaction.setOriginalAmount(new BigDecimal("200"));
        transaction.setOriginalCurrency("BRL");
        transaction.setType(TransactionType.DEPOSIT);

        AccountData account = new AccountData(
                1L, 123l,
                new BigDecimal("0"),
                new BigDecimal("1000"),
                Currency.getInstance("BRL")
        );

        processor.process(transaction, account);

        assertEquals(TransactionStatus.APPROVED, transaction.getStatus());
        assertEquals(new BigDecimal("200"), transaction.getConvertedAmount());
        assertEquals("BRL", transaction.getConvertedCurrency());
    }
}
