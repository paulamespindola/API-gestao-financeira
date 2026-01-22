package br.com.gestaofinanceira.service_user.domain.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(){
        super("Email já cadastrado.");
    }
}
