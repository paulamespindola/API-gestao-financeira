package br.com.gestaofinanceira.service_user.application.gateway;

import br.com.gestaofinanceira.service_user.application.command.CreateUserCommand;
import br.com.gestaofinanceira.service_user.domain.model.User;

import java.io.InputStream;
import java.util.List;

public interface UserBatchReader {
    List<CreateUserCommand> read(InputStream inputStream);
}
