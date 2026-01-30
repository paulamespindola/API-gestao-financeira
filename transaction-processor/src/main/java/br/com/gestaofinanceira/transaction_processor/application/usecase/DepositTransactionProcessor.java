package br.com.gestaofinanceira.transaction_processor.application.usecase;

import br.com.gestaofinanceira.transaction_processor.application.dto.AccountData;
import br.com.gestaofinanceira.transaction_processor.application.dto.AccountUpdateDto;
import br.com.gestaofinanceira.transaction_processor.application.gateway.AccountClient;
import br.com.gestaofinanceira.transaction_processor.application.gateway.TransactionProcessor;
import br.com.gestaofinanceira.transaction_processor.infrastructure.persistence.TransactionEntity;
import br.com.gestaofinanceira.transaction_processor.infrastructure.persistence.TransactionType;
import org.springframework.stereotype.Service;


@Service
public class DepositTransactionProcessor implements TransactionProcessor {

    private final AccountClient accountClient;

    public DepositTransactionProcessor(AccountClient accountClient) {
        this.accountClient = accountClient;
    }

    @Override
    public boolean supports(TransactionType type) {
        return type == TransactionType.DEPOSIT;
    }

    @Override
    public void process(TransactionEntity transaction, AccountData account) {

        transaction.approve(
                transaction.getOriginalAmount(),
                account.currency().getCurrencyCode()
        );

    }
}
