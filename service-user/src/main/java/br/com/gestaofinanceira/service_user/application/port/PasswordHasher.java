package br.com.gestaofinanceira.service_user.application.port;

public interface PasswordHasher {
    String hash(String rawPassword);
}
