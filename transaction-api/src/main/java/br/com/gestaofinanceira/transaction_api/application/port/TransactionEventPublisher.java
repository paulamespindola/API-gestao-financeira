package br.com.gestaofinanceira.transaction_api.application.port;

public interface TransactionEventPublisher {
    void publishTransactionCreated(String transactionId);
}
