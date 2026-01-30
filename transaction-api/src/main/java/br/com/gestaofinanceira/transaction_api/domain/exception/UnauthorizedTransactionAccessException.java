package br.com.gestaofinanceira.transaction_api.domain.exception;

public class UnauthorizedTransactionAccessException extends DomainException {
    public UnauthorizedTransactionAccessException() {
        super("You do not have permission to access this transaction");
    }
}
