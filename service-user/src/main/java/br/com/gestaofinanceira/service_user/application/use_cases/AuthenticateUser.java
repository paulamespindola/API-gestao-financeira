package br.com.gestaofinanceira.service_user.application.use_cases;

import br.com.gestaofinanceira.service_user.application.command.LoginCommand;
import br.com.gestaofinanceira.service_user.application.gateway.UserRepository;
import br.com.gestaofinanceira.service_user.application.port.AuthResult;
import br.com.gestaofinanceira.service_user.application.port.PasswordMatcher;
import br.com.gestaofinanceira.service_user.application.port.TokenGenerator;
import br.com.gestaofinanceira.service_user.domain.exception.InvalidCredentialsException;
import br.com.gestaofinanceira.service_user.domain.exception.UserNotFoundException;
import br.com.gestaofinanceira.service_user.domain.model.User;

public class AuthenticateUser {
    private final UserRepository userRepository;
    private final PasswordMatcher passwordMatcher;
    private final TokenGenerator tokenGenerator;

    public AuthenticateUser(UserRepository userRepository, PasswordMatcher passwordMatcher, TokenGenerator tokenGenerator) {
        this.userRepository = userRepository;
        this.passwordMatcher = passwordMatcher;
        this.tokenGenerator = tokenGenerator;
    }

    public AuthResult execute(LoginCommand command){
        User user = userRepository.findByEmail(command.email())
                .orElseThrow(UserNotFoundException::new);

        user.ensureCanAuthenticate();

        if (!passwordMatcher.matches(command.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("CPF ou senha inválidos");
        }

        return tokenGenerator.generate(user.getPublicId(), user.getEmail(), user.getRole());

    }
}
