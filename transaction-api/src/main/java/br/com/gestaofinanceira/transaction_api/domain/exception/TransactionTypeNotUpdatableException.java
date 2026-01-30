package br.com.gestaofinanceira.transaction_api.domain.exception;

public class TransactionTypeNotUpdatableException extends DomainException {

    public TransactionTypeNotUpdatableException() {
        super("This transaction type does not allow updates");
    }
}
