package br.com.gestaofinanceira.service_user.domain.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(){
        super("CPF já cadastrado");
    }
}
