package br.com.gestaofinanceira.transaction_api.domain.exception;

public class ApprovedTransactionModificationException extends DomainException {

    public ApprovedTransactionModificationException() {
        super("Approved transactions cannot be modified");
    }
}
