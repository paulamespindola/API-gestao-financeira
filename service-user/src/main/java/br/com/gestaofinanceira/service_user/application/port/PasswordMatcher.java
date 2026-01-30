package br.com.gestaofinanceira.service_user.application.port;

public interface PasswordMatcher {
    boolean matches(String raw, String hash);
}
