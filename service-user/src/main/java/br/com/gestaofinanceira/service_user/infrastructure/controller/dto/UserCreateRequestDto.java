package br.com.gestaofinanceira.service_user.infrastructure.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

public record UserCreateRequestDto(
        @CPF
        @NotBlank(message = "O campo CPF é obrigatório")
        String cpf,
        @NotBlank(message = "O campo nome é obrigatório")
        String name,
        @Email
        @NotBlank(message = "O campo email é obrigatório")
        String email,
        @NotBlank(message = "O campo senha é obrigatório")
        String passwordHash,
        @NotNull(message = "O campo data de nascimento é obrigatório")
        LocalDate birthDate
) {
}
