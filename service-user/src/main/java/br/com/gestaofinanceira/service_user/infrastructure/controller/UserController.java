package br.com.gestaofinanceira.service_user.infrastructure.controller;

import br.com.gestaofinanceira.service_user.application.BatchResult;
import br.com.gestaofinanceira.service_user.application.command.CreateUserCommand;
import br.com.gestaofinanceira.service_user.application.command.UpdateUserCommand;
import br.com.gestaofinanceira.service_user.application.use_cases.*;
import br.com.gestaofinanceira.service_user.domain.model.User;
import br.com.gestaofinanceira.service_user.infrastructure.controller.dto.UpdateUserDto;
import br.com.gestaofinanceira.service_user.infrastructure.controller.dto.UserCreateRequestDto;
import br.com.gestaofinanceira.service_user.infrastructure.controller.dto.UserResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private BatchCreateUserUseCase batchCreateUserUseCase;
    @Autowired
    private CreateUserUseCase createUserUseCase;
    @Autowired
    private DeleteUserUseCase deleteUserUseCase;
    @Autowired
    private GetUserUseCase getUserUseCase;
    @Autowired
    private ListUsersUseCase listUsersUseCase;
    @Autowired
    private UpdateUserUseCase updateUserUseCase;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserCreateRequestDto dto){
        CreateUserCommand command = new CreateUserCommand();
        command.cpf = dto.cpf();
        command.name = dto.name();
        command.passwordHash = dto.passwordHash();
        command.email = dto.email();
        command.birthDate = dto.birthDate();

        User user = createUserUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponseDto(user.getCpf(), user.getName(),
                user.getEmail(), user.getBirthDate()));
    }

    @PostMapping("/batch")
    public ResponseEntity<BatchResult> batchCreate(@RequestParam("file") MultipartFile file) {
        BatchResult result;
        try {
            result = batchCreateUserUseCase.execute(file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestParam String cpf){
        deleteUserUseCase.execute(cpf);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<UserResponseDto> getUser(@RequestParam String cpf){
        User user = getUserUseCase.execute(cpf);
        return ResponseEntity.ok().body(new UserResponseDto(user.getCpf(), user.getName(),
                user.getEmail(), user.getBirthDate()));
    }

    @PutMapping
    public ResponseEntity<UserResponseDto> updateUser(@RequestBody UpdateUserDto dto, @RequestParam String cpf){
        UpdateUserCommand command = new UpdateUserCommand();

        command.name = dto.name();
        command.email = dto.email();

        User user = updateUserUseCase.execute(command, cpf);

        return ResponseEntity.ok().body(new UserResponseDto(user.getCpf(), user.getName(),
                user.getEmail(), user.getBirthDate()));
    }

}
