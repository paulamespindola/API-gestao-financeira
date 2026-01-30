package br.com.gestaofinanceira.service_user.application.use_cases;

import br.com.gestaofinanceira.service_user.application.command.CreateUserCommand;
import br.com.gestaofinanceira.service_user.application.gateway.UserRepository;
import br.com.gestaofinanceira.service_user.application.port.PasswordHasher;
import br.com.gestaofinanceira.service_user.domain.exception.EmailAlreadyExistsException;
import br.com.gestaofinanceira.service_user.domain.exception.UserAlreadyExistsException;
import br.com.gestaofinanceira.service_user.domain.model.User;

public record CreateUserUseCase(UserRepository repository, PasswordHasher passwordHasher) {


    public User execute(CreateUserCommand command) {

        repository.findByCpf(command.cpf)
                .ifPresent(u -> {
                    throw new UserAlreadyExistsException();
                });

        repository.findByEmail(command.email)
                .ifPresent(u -> {
                    throw new EmailAlreadyExistsException();
                });

        String passwordHash = passwordHasher.hash(command.passwordHash);

        User user = User.create(
                command.cpf,
                command.name,
                command.email,
                passwordHash,
                command.birthDate
        );
        return repository.save(user);
    }
}
