package br.com.gestaofinanceira.service_user.domain.exception;

public class InvalidCpfException extends RuntimeException {
    public InvalidCpfException() {
        super("CPF inválido");
    }
}
