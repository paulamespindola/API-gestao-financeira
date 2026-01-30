package br.com.gestaofinanceira.service_user.application.use_cases;

import br.com.gestaofinanceira.service_user.application.gateway.UserRepository;
import br.com.gestaofinanceira.service_user.domain.exception.UserNotFoundException;
import br.com.gestaofinanceira.service_user.domain.model.User;

import java.util.UUID;

public record DeleteUserUseCase(UserRepository repository) {

    public void execute(UUID userId) {

        User user = repository.findByPublicId(userId)
                .orElseThrow(UserNotFoundException::new);

        user.deactivate();
        repository.update(user);
    }

}
