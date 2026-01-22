package br.com.gestaofinanceira.service_user.domain.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {
        super("O usuário não encontrado");
    }
}
