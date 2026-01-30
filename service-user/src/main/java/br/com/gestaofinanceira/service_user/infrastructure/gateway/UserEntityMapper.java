package br.com.gestaofinanceira.service_user.infrastructure.gateway;

import br.com.gestaofinanceira.service_user.domain.model.User;
import br.com.gestaofinanceira.service_user.infrastructure.persistence.UserEntity;

import java.time.LocalDateTime;
public class UserEntityMapper {

    public UserEntity toEntity(User user) {
        return new UserEntity(
                user.getPublicId(),
                user.getCpf(),
                user.getName(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getBirthDate(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getDeletedAt()
        );
    }

    public User toDomain(UserEntity entity) {
        return new User(
                entity.getPublicId(),
                entity.getCpf(),
                entity.getName(),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getBirthDate(),
                entity.getRole(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }

    public void updateEntity(UserEntity entity, User user) {
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        entity.setStatus(user.getStatus());
        entity.setDeletedAt(user.getDeletedAt());
        entity.setUpdatedAt(LocalDateTime.now());
    }
}
