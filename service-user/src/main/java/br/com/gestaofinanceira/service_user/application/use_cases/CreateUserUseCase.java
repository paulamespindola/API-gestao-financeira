package br.com.gestaofinanceira.service_user.application.use_cases;

import br.com.gestaofinanceira.service_user.application.command.CreateUserCommand;
import br.com.gestaofinanceira.service_user.application.gateway.UserRepository;
import br.com.gestaofinanceira.service_user.domain.exception.EmailAlreadyExistsException;
import br.com.gestaofinanceira.service_user.domain.exception.UserAlreadyExistsException;
import br.com.gestaofinanceira.service_user.domain.model.User;

public record CreateUserUseCase(UserRepository repository) {


    public User execute(CreateUserCommand command) {

        repository.findByCpf(command.cpf)
                .ifPresent(u -> {
                    throw new UserAlreadyExistsException();
                });

        repository.findByEmail(command.email)
                .ifPresent(u -> {
                    throw new EmailAlreadyExistsException();
                });

        User user = User.create(
                command.cpf,
                command.name,
                command.email,
                command.passwordHash,
                command.birthDate
        );
        return repository.save(user);
    }
}
