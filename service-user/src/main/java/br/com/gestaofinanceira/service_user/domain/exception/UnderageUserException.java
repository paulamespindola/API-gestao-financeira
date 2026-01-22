package br.com.gestaofinanceira.service_user.domain.exception;

public class UnderageUserException extends RuntimeException {
    public UnderageUserException() {
        super("O usuário deve ter pelo menos 18 anos");
    }
}
