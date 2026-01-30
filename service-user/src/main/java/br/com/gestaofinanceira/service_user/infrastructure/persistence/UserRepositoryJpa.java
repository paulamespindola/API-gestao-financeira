package br.com.gestaofinanceira.service_user.infrastructure.persistence;

import br.com.gestaofinanceira.service_user.domain.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface UserRepositoryJpa extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByCpf(String cpf);

    Page<UserEntity> findAllByStatus(Status status, Pageable pageable);

    Optional<UserEntity> findByPublicId(UUID id);

    Optional<UserEntity> findByEmail(String email);
}
