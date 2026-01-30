package br.com.gestaofinanceira.service_user.application.gateway;

import br.com.gestaofinanceira.service_user.domain.model.User;
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);

    User update(User user);

    Optional<User> findByCpf(String cpf);

    Optional<User> findByPublicId(UUID id);

    Page<User> findAllActiveTrue(Pageable pageable);

    Optional<User> findByEmail(String email);
}
