package br.com.gestaofinanceira.service_user.application.use_cases;

import br.com.gestaofinanceira.service_user.application.command.CreateUserCommand;
import br.com.gestaofinanceira.service_user.application.gateway.UserRepository;
import br.com.gestaofinanceira.service_user.domain.exception.EmailAlreadyExistsException;
import br.com.gestaofinanceira.service_user.domain.exception.UserAlreadyExistsException;
import br.com.gestaofinanceira.service_user.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateUserUseCaseTest {

    private UserRepository repository;
    private CreateUserUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(UserRepository.class);
        useCase = new CreateUserUseCase(repository);
    }

    private CreateUserCommand validCommand() {
        return new CreateUserCommand(
                "12345678900",
                "João",
                "email@email.com",
                "hash",
                LocalDate.now().minusYears(20)
        );
    }

    @Test
    @DisplayName("deve criar usuário quando cpf e email não existirem")
    void shouldCreateUserWhenCpfAndEmailDoNotExist() {
        CreateUserCommand command = validCommand();

        when(repository.findByCpf(command.cpf)).thenReturn(Optional.empty());
        when(repository.findByEmail(command.email)).thenReturn(Optional.empty());
        when(repository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = useCase.execute(command);

        assertNotNull(result);
        assertEquals(command.cpf, result.getCpf());
        assertEquals(command.email, result.getEmail());

        verify(repository).save(any(User.class));
    }

    @Test
    @DisplayName("deve lançar exceção quando cpf já existir")
    void shouldThrowExceptionWhenCpfAlreadyExists() {
        CreateUserCommand command = validCommand();

        when(repository.findByCpf(command.cpf))
                .thenReturn(Optional.of(mock(User.class)));

        assertThrows(
                UserAlreadyExistsException.class,
                () -> useCase.execute(command)
        );

        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("deve lançar exceção quando email já existir")
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        CreateUserCommand command = validCommand();

        when(repository.findByCpf(command.cpf))
                .thenReturn(Optional.empty());

        when(repository.findByEmail(command.email))
                .thenReturn(Optional.of(mock(User.class)));

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> useCase.execute(command)
        );

        verify(repository, never()).save(any());
    }
}
