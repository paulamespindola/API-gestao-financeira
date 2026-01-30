package br.com.gestaofinanceira.transaction_api.application;

public record TransactionRequestEvent(
        String transactionId
) {
}
