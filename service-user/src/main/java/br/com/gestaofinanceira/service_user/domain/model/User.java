package br.com.gestaofinanceira.service_user.domain.model;

import br.com.gestaofinanceira.service_user.domain.exception.InvalidCpfException;
import br.com.gestaofinanceira.service_user.domain.exception.InvalidEmailException;
import br.com.gestaofinanceira.service_user.domain.exception.UnderageUserException;
import br.com.gestaofinanceira.service_user.domain.exception.UserAlreadyInactiveException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.UUID;

public class User {

    private final UUID publicId;

    private final String cpf;
    private final String passwordHash;

    private String name;
    private String email;
    private Status status;

    private final LocalDate birthDate;
    private final Role role;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public User(
            UUID publicId,
            String cpf,
            String name,
            String email,
            String passwordHash,
            LocalDate birthDate,
            Role role,
            Status status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime deletedAt
    ) {
        this.publicId = publicId;
        this.cpf = cpf;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.birthDate = birthDate;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static User create(
            String cpf,
            String name,
            String email,
            String passwordHash,
            LocalDate birthDate
    ) {
        validateCpf(cpf);
        validateAge(birthDate);
        validateEmail(email);

        return new User(
                UUID.randomUUID(),
                cpf,
                name,
                normalizeEmail(email),
                passwordHash,
                birthDate,
                Role.USER,
                Status.ACTIVE,
                LocalDateTime.now(),
                null,
                null
        );
    }

    public void deactivate() {
        if (status == Status.INACTIVE) {
            throw new UserAlreadyInactiveException();
        }
        this.status = Status.INACTIVE;
        this.deletedAt = LocalDateTime.now();
        touch();
    }

    public void updateEmail(String newEmail) {
        validateEmail(newEmail);
        this.email = normalizeEmail(newEmail);
        touch();
    }

    public void updateName(String name) {
        this.name = name;
        touch();
    }

    public void updatedAtNew(){
        this.updatedAt = LocalDateTime.now();
    }
    public void ensureCanAuthenticate() {
        if (!isActive()) {
            throw new UserAlreadyInactiveException();
        }
    }

    public boolean isActive() {
        return status == Status.ACTIVE && deletedAt == null;
    }

    private void touch() {
        this.updatedAt = LocalDateTime.now();
    }

    private static void validateCpf(String cpf) {
        if (cpf == null || !cpf.matches("\\d{11}")) {
            throw new InvalidCpfException();
        }
    }

    private static void validateAge(LocalDate birthDate) {
        int age = Period.between(birthDate, LocalDate.now()).getYears();
        if (age < 18) {
            throw new UnderageUserException();
        }
    }

    private static void validateEmail(String email) {
        if (email == null || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new InvalidEmailException();
        }
    }

    private static String normalizeEmail(String email) {
        return email.toLowerCase().trim();
    }

    public UUID getPublicId() {
        return publicId;
    }

    public String getCpf() {
        return cpf;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Role getRole() {
        return role;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }
}
