package br.com.gestaofinanceira.service_user.infrastructure.gateway;

import br.com.gestaofinanceira.service_user.application.gateway.UserRepository;
import br.com.gestaofinanceira.service_user.domain.model.Status;
import br.com.gestaofinanceira.service_user.domain.model.User;
import br.com.gestaofinanceira.service_user.infrastructure.persistence.UserEntity;
import br.com.gestaofinanceira.service_user.infrastructure.persistence.UserRepositoryJpa;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryAdapter implements UserRepository {

    private final UserRepositoryJpa repository;
    private final UserEntityMapper mapper;

    public UserRepositoryAdapter(UserRepositoryJpa repository, UserEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {

        UserEntity entity = repository.findByCpf(user.getCpf())
                .orElseGet(UserEntity::new);

        mapper.updateEntity(entity, user);

        UserEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<User> findByCpf(String cpf) {
        return repository.findByCpf(cpf)
                .map(mapper::toDomain);
    }

    @Override
    public List<User> findAllActive() {
        return repository.findAllByStatus(Status.ACTIVE)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByCpf(email)
                .map(mapper::toDomain);
    }
}
