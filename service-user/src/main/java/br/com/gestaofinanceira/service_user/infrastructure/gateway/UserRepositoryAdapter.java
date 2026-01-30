package br.com.gestaofinanceira.service_user.infrastructure.gateway;

import br.com.gestaofinanceira.service_user.application.gateway.UserRepository;
import br.com.gestaofinanceira.service_user.domain.exception.UserNotFoundException;
import br.com.gestaofinanceira.service_user.domain.model.Status;
import br.com.gestaofinanceira.service_user.domain.model.User;
import br.com.gestaofinanceira.service_user.infrastructure.persistence.UserEntity;
import br.com.gestaofinanceira.service_user.infrastructure.persistence.UserRepositoryJpa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

        UserEntity entity = mapper.toEntity(user);

        UserEntity saved = repository.save(entity);

        return mapper.toDomain(saved);
    }

    @Override
    public User update(User user) {

        UserEntity entity = repository.findByCpf(user.getCpf())
                .orElseThrow(UserNotFoundException::new);

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
    public Optional<User> findByPublicId(UUID id) {
        return repository.findByPublicId(id)
                .map(mapper::toDomain);
    }

    @Override
    public Page<User> findAllActiveTrue(Pageable pageable) {
        return repository
                .findAllByStatus(Status.ACTIVE, pageable)
                .map(mapper::toDomain);
    }


    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(mapper::toDomain);
    }


}
