package br.com.gestaofinanceira.transaction_processor.application.usecase;

import br.com.gestaofinanceira.transaction_processor.application.dto.AccountData;
import br.com.gestaofinanceira.transaction_processor.application.dto.AccountUpdateDto;
import br.com.gestaofinanceira.transaction_processor.application.dto.ExchangeRate;
import br.com.gestaofinanceira.transaction_processor.application.gateway.AccountClient;
import br.com.gestaofinanceira.transaction_processor.application.gateway.ExchangeRateClient;
import br.com.gestaofinanceira.transaction_processor.application.gateway.TransactionProcessor;
import br.com.gestaofinanceira.transaction_processor.infrastructure.persistence.TransactionEntity;
import br.com.gestaofinanceira.transaction_processor.infrastructure.persistence.TransactionType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public class DebitTransactionProcessor implements TransactionProcessor {

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

        BigDecimal amountToDebit = convert(transaction, account);

        if (account.balance().compareTo(amountToDebit) < 0) {
            transaction.reject("Insufficient balance");
            return;
        }

        if (requiresDailyLimit(transaction.getType())
                && account.dailyLimit().compareTo(amountToDebit) < 0) {
            transaction.reject("Daily limit exceeded");
            return;
        }

        transaction.approve(
                amountToDebit,
                account.currency().getCurrencyCode()
        );
    }

    //encapsular esse convert em algum lugar, pois vou precisar deles em vários use cases
    private BigDecimal convert(TransactionEntity transaction, AccountData account) {

        String transactionCurrency = transaction.getOriginalCurrency();
        String accountCurrency = account.currency().getCurrencyCode();

        if (transactionCurrency.equals(accountCurrency)) {
            return transaction.getOriginalAmount();
        }

        ExchangeRate rate = exchangeClient.getRate(
                transactionCurrency,
                accountCurrency
        );

        return transaction.getOriginalAmount()
                .multiply(rate.rate());
    }

    private boolean requiresDailyLimit(TransactionType type) {
        return type == TransactionType.PURCHASE
                || type == TransactionType.PAYMENT
                || type == TransactionType.WITHDRAW;
    }
}
