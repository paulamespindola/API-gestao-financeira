package br.com.gestaofinanceira.transaction_processor.application.gateway;

import br.com.gestaofinanceira.transaction_processor.application.dto.AccountData;
import br.com.gestaofinanceira.transaction_processor.infrastructure.persistence.TransactionEntity;
import br.com.gestaofinanceira.transaction_processor.infrastructure.persistence.TransactionType;

public interface TransactionProcessor {

    boolean supports(TransactionType type);

    void process(TransactionEntity transaction, AccountData account);
}
