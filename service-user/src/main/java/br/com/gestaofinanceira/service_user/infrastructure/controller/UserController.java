package br.com.gestaofinanceira.service_user.infrastructure.controller;

import br.com.gestaofinanceira.service_user.application.use_cases.BatchResult;
import br.com.gestaofinanceira.service_user.application.command.CreateUserCommand;
import br.com.gestaofinanceira.service_user.application.command.UpdateUserCommand;
import br.com.gestaofinanceira.service_user.application.use_cases.*;
import br.com.gestaofinanceira.service_user.domain.model.User;
import br.com.gestaofinanceira.service_user.infrastructure.controller.dto.PageResponse;
import br.com.gestaofinanceira.service_user.infrastructure.controller.dto.UpdateUserDto;
import br.com.gestaofinanceira.service_user.infrastructure.controller.dto.UserCreateRequestDto;
import br.com.gestaofinanceira.service_user.infrastructure.controller.dto.UserResponseDto;
import br.com.gestaofinanceira.service_user.infrastructure.security.UserDetailsAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

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
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponseDto(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
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
    public ResponseEntity<Void> deleteUser(Authentication authentication){
        UserDetailsAdapter userDetails = (UserDetailsAdapter) authentication.getPrincipal();
        UUID userId = userDetails.getPublicId();

        deleteUserUseCase.execute(userId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<UserResponseDto> getUser(Authentication authentication){
        UserDetailsAdapter userDetails = (UserDetailsAdapter) authentication.getPrincipal();
        UUID userId = userDetails.getPublicId();

        User user = getUserUseCase.execute(userId);
        return ResponseEntity.ok().body(new UserResponseDto(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<PageResponse<UserResponseDto>> getAllUsers(
            Authentication authentication,
            @PageableDefault(size = 10, sort = {"name"}) Pageable pageable
    ) {
        UserDetailsAdapter userDetails = (UserDetailsAdapter) authentication.getPrincipal();
        UUID userId = userDetails.getPublicId();

        Page<UserResponseDto> page = listUsersUseCase
                .execute(pageable, userId)
                .map(UserResponseDto::new);

        return ResponseEntity.ok(PageResponse.from(page));
    }


    @PutMapping
    public ResponseEntity<UserResponseDto> updateUser(@RequestBody UpdateUserDto dto, Authentication authentication){
        UserDetailsAdapter userDetails = (UserDetailsAdapter) authentication.getPrincipal();
        UUID userId = userDetails.getPublicId();

        UpdateUserCommand command = new UpdateUserCommand();
        command.name = dto.name();
        command.email = dto.email();

        User user = updateUserUseCase.execute(command, userId);

        return ResponseEntity.ok().body(new UserResponseDto(user));
    }

}
