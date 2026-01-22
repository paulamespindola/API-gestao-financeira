package br.com.gestaofinanceira.service_user.application.use_cases;

import br.com.gestaofinanceira.service_user.application.gateway.UserRepository;
import br.com.gestaofinanceira.service_user.domain.model.User;

import java.util.List;

public record ListUsersUseCase(UserRepository repository) {

    public List<User> execute() {
        return repository.findAllActive();
    }

}
