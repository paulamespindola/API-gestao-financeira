package br.com.gestaofinanceira.transaction_api.domain.exception;

public class InvalidTransactionStateException extends DomainException {
    public InvalidTransactionStateException(String message) {
        super(message);
    }
}
