package br.com.gestaofinanceira.transaction_api.application.port;

import br.com.gestaofinanceira.transaction_api.application.TransactionDlqEvent;

public interface TransactionEventDlq {
    void publishTransactionDlq(TransactionDlqEvent transactionDlqEvent);
}
