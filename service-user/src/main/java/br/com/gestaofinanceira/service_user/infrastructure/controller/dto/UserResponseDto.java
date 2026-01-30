package br.com.gestaofinanceira.service_user.infrastructure.controller.dto;


import br.com.gestaofinanceira.service_user.domain.model.User;

import java.time.LocalDate;

public record UserResponseDto(
        String cpf,
        String name,
        String email,
        LocalDate birthDate) {

    public UserResponseDto(User user){
        this(user.getCpf(), user.getName(), user.getEmail(), user.getBirthDate());
    }
}
