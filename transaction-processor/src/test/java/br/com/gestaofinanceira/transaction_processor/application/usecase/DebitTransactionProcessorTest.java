package br.com.gestaofinanceira.transaction_processor.application.usecase;

import br.com.gestaofinanceira.transaction_processor.application.dto.AccountData;
import br.com.gestaofinanceira.transaction_processor.application.dto.ExchangeRate;
import br.com.gestaofinanceira.transaction_processor.application.gateway.AccountClient;
import br.com.gestaofinanceira.transaction_processor.application.gateway.ExchangeRateClient;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebitTransactionProcessorTest {

    @Mock
    AccountClient accountClient;

    @Mock
    ExchangeRateClient exchangeRateClient;

    @InjectMocks
    DebitTransactionProcessor processor;

    @Test
    void shouldApproveDebitWhenBalanceIsEnough_sameCurrency() {

        TransactionEntity transaction = new TransactionEntity();
        transaction.setOriginalAmount(new BigDecimal("100"));
        transaction.setOriginalCurrency("BRL");
        transaction.setType(TransactionType.PURCHASE);

        AccountData account = new AccountData(
                1L, 123l,
                new BigDecimal("500"),
                new BigDecimal("1000"),
                Currency.getInstance("BRL")
        );

        processor.process(transaction, account);

        assertEquals(TransactionStatus.APPROVED, transaction.getStatus());
        assertEquals(new BigDecimal("100"), transaction.getConvertedAmount());
        assertEquals("BRL", transaction.getConvertedCurrency());
    }

    @Test
    void shouldRejectWhenBalanceIsInsufficient() {

        TransactionEntity transaction = new TransactionEntity();
        transaction.setOriginalAmount(new BigDecimal("500"));
        transaction.setOriginalCurrency("BRL");
        transaction.setType(TransactionType.PURCHASE);

        AccountData account = new AccountData(
                1L, 123l,
                new BigDecimal("100"),
                new BigDecimal("1000"),
                Currency.getInstance("BRL")
        );

        processor.process(transaction, account);

        assertEquals(TransactionStatus.REJECTED, transaction.getStatus());
        assertEquals("Insufficient balance", transaction.getRejectionReason());
    }

    @Test
    void shouldConvertCurrencyWhenDifferent() {

        when(exchangeRateClient.getRate("USD", "BRL"))
                .thenReturn(new ExchangeRate(new BigDecimal("5.0"), "USD", "BRL"));

        TransactionEntity transaction = new TransactionEntity();
        transaction.setOriginalAmount(new BigDecimal("10"));
        transaction.setOriginalCurrency("USD");
        transaction.setType(TransactionType.PURCHASE);

        AccountData account = new AccountData(
                1L, 123l,
                new BigDecimal("100"),
                new BigDecimal("1000"),
                Currency.getInstance("BRL")
        );

        processor.process(transaction, account);

        assertEquals(new BigDecimal("50.0"), transaction.getConvertedAmount());
        verify(exchangeRateClient).getRate("USD", "BRL");
    }
}
