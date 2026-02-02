package br.com.gestaofinanceira.transaction_processor.application.usecase;

import br.com.gestaofinanceira.transaction_processor.application.dto.AccountData;
import br.com.gestaofinanceira.transaction_processor.application.dto.ExchangeRate;
import br.com.gestaofinanceira.transaction_processor.application.exception.BusinessException;
import br.com.gestaofinanceira.transaction_processor.application.gateway.AccountClient;
import br.com.gestaofinanceira.transaction_processor.application.gateway.ExchangeRateClient;
import br.com.gestaofinanceira.transaction_processor.application.gateway.TransactionProcessor;
import br.com.gestaofinanceira.transaction_processor.infrastructure.persistence.TransactionEntity;
import br.com.gestaofinanceira.transaction_processor.infrastructure.persistence.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;

@Service
public class DebitTransactionProcessor implements TransactionProcessor {

    private static final Logger log = LoggerFactory.getLogger(DebitTransactionProcessor.class);

    private final AccountClient accountClient;
    private final ExchangeRateClient exchangeClient;

    public DebitTransactionProcessor(AccountClient accountClient,
                                     ExchangeRateClient exchangeClient) {
        this.accountClient = accountClient;
        this.exchangeClient = exchangeClient;
    }

    @Override
    public boolean supports(TransactionType type) {
        return type != TransactionType.DEPOSIT;
    }

    @Override
    public void process(TransactionEntity transaction, AccountData account) {

        if (account == null || account.currency() == null) {
            throw new BusinessException("Conta ou moeda inválida para transação " + transaction.getId());
        }

        BigDecimal amountToDebit = convert(transaction, account);

        log.info("Processando transação {} do tipo {} para usuário {}. Valor convertido: {} {}",
                transaction.getId(),
                transaction.getType(),
                transaction.getUserId(),
                amountToDebit,
                account.currency().getCurrencyCode()
        );

        if (account.balance().compareTo(amountToDebit) < 0) {
            transaction.reject("Insufficient balance");
            log.warn("Transação {} rejeitada: saldo insuficiente", transaction.getId());
            return;
        }

        if (requiresDailyLimit(transaction.getType()) &&
                account.dailyLimit().compareTo(amountToDebit) < 0) {
            transaction.reject("Daily limit exceeded");
            log.warn("Transação {} rejeitada: limite diário excedido", transaction.getId());
            return;
        }

        transaction.approve(
                amountToDebit,
                account.currency().getCurrencyCode()
        );

        log.info("Transação {} aprovada com sucesso", transaction.getId());
    }

    private BigDecimal convert(TransactionEntity transaction, AccountData account) {
        String transactionCurrency = transaction.getOriginalCurrency();
        String accountCurrency = account.currency().getCurrencyCode();

        if (transactionCurrency.equals(accountCurrency)) {
            return transaction.getOriginalAmount();
        }

        ExchangeRate rate = exchangeClient.getRate(transactionCurrency, accountCurrency);
        if (rate == null || rate.rate() == null) {
            throw new BusinessException("Taxa de câmbio inválida para transação " + transaction.getId());
        }

        return transaction.getOriginalAmount()
                .multiply(rate.rate(), MathContext.DECIMAL64);
    }

    private boolean requiresDailyLimit(TransactionType type) {
        return type == TransactionType.PURCHASE
                || type == TransactionType.PAYMENT
                || type == TransactionType.WITHDRAW;
    }
}
