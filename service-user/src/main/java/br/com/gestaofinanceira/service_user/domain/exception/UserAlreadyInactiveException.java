package br.com.gestaofinanceira.service_user.domain.exception;

public class UserAlreadyInactiveException extends RuntimeException {
    public UserAlreadyInactiveException() {
        super("O usuário já esta inativo");
    }
}
