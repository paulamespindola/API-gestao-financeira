package br.com.gestaofinanceira.service_user.infrastructure.controller.dto;


import java.time.LocalDate;

public record UserResponseDto(
        String cpf,
        String name,
        String email,
        LocalDate birthDate) {
}
