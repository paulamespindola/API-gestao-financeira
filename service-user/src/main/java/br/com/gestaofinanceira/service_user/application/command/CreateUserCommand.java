package br.com.gestaofinanceira.service_user.application.command;

import java.time.LocalDate;

public class CreateUserCommand {
    public String cpf;
    public String name;
    public String email;
    public String passwordHash;
    public LocalDate birthDate;

    public CreateUserCommand(String cpf, String name, String email, String passwordHash, LocalDate birthDate) {
        this.cpf = cpf;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.birthDate = birthDate;
    }

    public CreateUserCommand() {
    }
}