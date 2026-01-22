package br.com.gestaofinanceira.service_user.infrastructure.persistence;

import br.com.gestaofinanceira.service_user.domain.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepositoryJpa extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByCpf(String cpf);

    List<UserEntity> findAllByStatus(Status status);
}
