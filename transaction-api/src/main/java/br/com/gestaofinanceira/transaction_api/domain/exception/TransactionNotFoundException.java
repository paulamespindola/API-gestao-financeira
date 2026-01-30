package br.com.gestaofinanceira.transaction_api.domain.exception;

public class TransactionNotFoundException extends DomainException {
    public TransactionNotFoundException() {
        super("Transaction not found");
    }
}
