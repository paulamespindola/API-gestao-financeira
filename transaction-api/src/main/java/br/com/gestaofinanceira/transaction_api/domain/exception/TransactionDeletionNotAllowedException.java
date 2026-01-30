package br.com.gestaofinanceira.transaction_api.domain.exception;

public class TransactionDeletionNotAllowedException extends DomainException {

    public TransactionDeletionNotAllowedException() {
        super("Only transactions with PENDING status can be deleted");
    }
}
