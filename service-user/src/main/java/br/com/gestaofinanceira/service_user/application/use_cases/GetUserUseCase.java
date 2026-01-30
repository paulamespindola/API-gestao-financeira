package br.com.gestaofinanceira.service_user.application.use_cases;

import br.com.gestaofinanceira.service_user.application.gateway.UserRepository;
import br.com.gestaofinanceira.service_user.domain.exception.UserNotFoundException;
import br.com.gestaofinanceira.service_user.domain.model.User;

import java.util.UUID;

public record GetUserUseCase(UserRepository repository) {

    public User execute(UUID userId) {
        User user =  repository.findByPublicId(userId)
                .orElseThrow(UserNotFoundException::new);

        if(!user.isActive()){
            throw new UserNotFoundException();
        }

        return user;
    }

}
