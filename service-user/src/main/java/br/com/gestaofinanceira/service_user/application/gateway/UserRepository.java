package br.com.gestaofinanceira.service_user.application.gateway;

import br.com.gestaofinanceira.service_user.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);

    Optional<User> findByCpf(String cpf);

    List<User> findAllActive();

    Optional<User> findByEmail(String email);
}
