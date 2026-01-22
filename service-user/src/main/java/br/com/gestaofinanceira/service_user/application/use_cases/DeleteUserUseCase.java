package br.com.gestaofinanceira.service_user.application.use_cases;

import br.com.gestaofinanceira.service_user.application.gateway.UserRepository;
import br.com.gestaofinanceira.service_user.domain.exception.UserNotFoundException;
import br.com.gestaofinanceira.service_user.domain.model.User;

public record DeleteUserUseCase(UserRepository repository) {

    public void execute(String cpf) {

        User user = repository.findByCpf(cpf)
                .orElseThrow(UserNotFoundException::new);

        user.deactivate();
        repository.save(user);
    }

}
