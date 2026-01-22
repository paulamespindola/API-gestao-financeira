package br.com.gestaofinanceira.service_user.domain.model;

import br.com.gestaofinanceira.service_user.domain.exception.InvalidCpfException;
import br.com.gestaofinanceira.service_user.domain.exception.InvalidEmailException;
import br.com.gestaofinanceira.service_user.domain.exception.UnderageUserException;
import br.com.gestaofinanceira.service_user.domain.exception.UserAlreadyInactiveException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Objects;

public class User {
    private final String cpf;
    private String name;
    private String email;
    private final String passwordHash;
    private final LocalDate birthDate;
    private final Role role;
    private Status status;
    private final LocalDateTime createdAt;
    private LocalDateTime updateAt;
    private LocalDateTime deletedAt;

    public User(String cpf, String name, String email, String passwordHash, LocalDate birthDate) {
        this.cpf = cpf;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.birthDate = birthDate;
        this.role = Role.USER;
        this.status = Status.ACTIVE;
        this.createdAt = LocalDateTime.now();
    }

    public User(String cpf, String name, String email, String passwordHash, LocalDate birthDate,
                Role role, Status status, LocalDateTime createdAt, LocalDateTime updateAt,
                LocalDateTime deletedAt) {
        this.cpf = cpf;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.birthDate = birthDate;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
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

        return new User(cpf, name, normalizeEmail(email), passwordHash, birthDate);
    }

    public void deactivate() {
        if (this.status == Status.INACTIVE) {
            throw new UserAlreadyInactiveException();
        }
        this.status = Status.INACTIVE;
        this.deletedAt = LocalDateTime.now();
    }

    public void updateEmail(String newEmail) {
        validateEmail(newEmail);
        this.email = normalizeEmail(newEmail);
    }

    public void updateName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return this.status == Status.ACTIVE && this.deletedAt == null;
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

    public void updateAt(){
        updateAt = LocalDateTime.now();
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

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

}
