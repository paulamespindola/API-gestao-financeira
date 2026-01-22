package br.com.gestaofinanceira.service_user.domain.model;

import br.com.gestaofinanceira.service_user.domain.exception.InvalidCpfException;
import br.com.gestaofinanceira.service_user.domain.exception.InvalidEmailException;
import br.com.gestaofinanceira.service_user.domain.exception.UnderageUserException;
import br.com.gestaofinanceira.service_user.domain.exception.UserAlreadyInactiveException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("Deve criar usuario quando cpf e idade sao validos")
    void shouldCreateUserWhenCpfAndAgeAreValid() {
        User user = User.create(
                "12345678900",
                "Teste",
                "teste@teste.com",
                "hash",
                LocalDate.now().minusYears(20)
        );

        assertNotNull(user);
        assertEquals("12345678900", user.getCpf());
    }

    @Test
    @DisplayName("Deve lançar exceção quando cpf for inválido")
    void shouldThrowExceptionWhenCpfIsInvalid() {
        assertThrows(InvalidCpfException.class, () ->
                User.create(
                        "123",
                        "teste",
                        "teste@teste.com",
                        "hash",
                        LocalDate.now().minusYears(20)
                )
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuario for menor de idade")
    void shouldThrowExceptionWhenUserIsUnderage() {
        assertThrows(UnderageUserException.class, () ->
                User.create(
                        "12345678900",
                        "Teste",
                        "teste@teste.com",
                        "hash",
                        LocalDate.now().minusYears(10)
                )
        );
    }

    @Test
    @DisplayName("Deve lançar exceção quando email for inválido")
    void shouldThrowExceptionWhenEmailIsInvalid() {
        assertThrows(InvalidEmailException.class, () ->
                User.create(
                        "12345678900",
                        "Teste",
                        "teste",
                        "hash",
                        LocalDate.now().minusYears(20)
                )
        );
    }

    @Test
    @DisplayName("não deve lançar exceção quando email for válido")
    void shouldNotThrowExceptionWhenEmailIsValid() {
        assertDoesNotThrow(() ->
                User.create(
                        "12345678900",
                        "Teste",
                        "teste@teste.com",
                        "hash",
                        LocalDate.now().minusYears(20)
                )
        );
    }

    @Test
    @DisplayName("deve desativar o usuário quando estiver ativo")
    void shouldDeactivateUserWhenActive() {
        User user = User.create(
                "12345678900",
                "João",
                "email@email.com",
                "hash",
                LocalDate.now().minusYears(20)
        );

        user.deactivate();

        assertEquals(Status.INACTIVE, user.getStatus());
        assertNotNull(user.getDeletedAt());
    }

    @Test
    @DisplayName("deve lançar exceção quando usuário já estiver inativo")
    void shouldThrowExceptionWhenUserIsAlreadyInactive() {
        User user = User.create(
                "12345678900",
                "João",
                "email@email.com",
                "hash",
                LocalDate.now().minusYears(20)
        );

        user.deactivate();

        assertThrows(
                UserAlreadyInactiveException.class,
                user::deactivate
        );
    }

}
