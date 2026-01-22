package br.com.gestaofinanceira.service_user.infrastructure.gateway;

import br.com.gestaofinanceira.service_user.domain.model.User;
import br.com.gestaofinanceira.service_user.infrastructure.persistence.UserEntity;

import java.time.LocalDateTime;

public class UserEntityMapper {

    public UserEntity toEntity(User user){
        return new UserEntity(user.getCpf(), user.getName(), user.getEmail(),
                user.getPasswordHash(), user.getBirthDate(), user.getRole(),
                user.getStatus(), user.getCreatedAt(), user.getUpdateAt(), user.getDeletedAt());
    }

    public User toDomain(UserEntity entity){
        return new User(entity.getCpf(), entity.getName(), entity.getEmail(),
                entity.getPasswordHash(), entity.getBirthDate(), entity.getRole(),
                entity.getStatus(), entity.getCreatedAt(), entity.getUpdatedAt(), entity.getDeletedAt());
    }

    public void updateEntity(UserEntity entity, User user) {
        entity.setCpf(user.getCpf());
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        entity.setPasswordHash(user.getPasswordHash());
        entity.setBirthDate(user.getBirthDate());
        entity.setStatus(user.getStatus());
        entity.setRole(user.getRole());
        entity.setDeletedAt(user.getDeletedAt());
        entity.setUpdatedAt(LocalDateTime.now());
    }

}
