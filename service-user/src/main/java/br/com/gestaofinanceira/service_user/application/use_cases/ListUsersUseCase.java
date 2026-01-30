package br.com.gestaofinanceira.service_user.application.use_cases;

import br.com.gestaofinanceira.service_user.application.gateway.UserRepository;
import br.com.gestaofinanceira.service_user.domain.exception.UserNotFoundException;
import br.com.gestaofinanceira.service_user.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;


public record ListUsersUseCase(UserRepository repository) {

    public Page<User> execute(Pageable pageable, UUID userId) {
        User user =  repository.findByPublicId(userId)
                .orElseThrow(UserNotFoundException::new);

        return repository.findAllActiveTrue(pageable);
    }

}
