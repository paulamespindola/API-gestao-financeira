package br.com.gestaofinanceira.transaction_processor.application.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
