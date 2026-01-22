package br.com.gestaofinanceira.service_user.domain.exception;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException() {
        super("Email inválido");
    }
}
