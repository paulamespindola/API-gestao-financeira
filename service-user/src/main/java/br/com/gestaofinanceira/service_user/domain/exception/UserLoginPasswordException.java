package br.com.gestaofinanceira.service_user.domain.exception;

public class UserLoginPasswordException extends RuntimeException {
    public UserLoginPasswordException(){
        super("Password invalid.");
    }
}
