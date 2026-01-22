package br.com.gestaofinanceira.service_user.infrastructure.persistence;

import br.com.gestaofinanceira.service_user.domain.model.Role;
import br.com.gestaofinanceira.service_user.domain.model.Status;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class UserEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cpf;
    private String name;
    private String email;
    private String passwordHash;
    private LocalDate birthDate;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public UserEntity(String cpf, String name, String email, String passwordHash,
                      LocalDate birthDate, Role role, Status status, LocalDateTime createdAt,
                      LocalDateTime updateAt, LocalDateTime deletedAt) {
        this.cpf = cpf;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.birthDate = birthDate;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updateAt;
        this.deletedAt = deletedAt;
    }
}
