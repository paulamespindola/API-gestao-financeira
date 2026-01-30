package br.com.gestaofinanceira.service_user.application.use_cases;

import br.com.gestaofinanceira.service_user.application.command.UpdateUserCommand;
import br.com.gestaofinanceira.service_user.application.gateway.UserRepository;
import br.com.gestaofinanceira.service_user.domain.exception.EmailAlreadyExistsException;
import br.com.gestaofinanceira.service_user.domain.exception.UserNotFoundException;
import br.com.gestaofinanceira.service_user.domain.model.User;

import java.util.UUID;

public record UpdateUserUseCase(UserRepository repository) {

    public User execute(UpdateUserCommand command, UUID publicId) {

        User user = repository.findByPublicId(publicId)
                .orElseThrow(UserNotFoundException::new);

        repository.findByEmail(command.email)
                .ifPresent(u -> {
                    throw new EmailAlreadyExistsException();
                });

        if (command.name != null) {
            user.updateName(command.name);
        }

        if (command.email != null) {
            user.updateEmail(command.email);
        }

        user.updatedAtNew();

        return repository.update(user);

    }

}
