package br.com.gestaofinanceira.service_user.application.use_cases;

import br.com.gestaofinanceira.service_user.application.BatchResult;
import br.com.gestaofinanceira.service_user.application.command.CreateUserCommand;
import br.com.gestaofinanceira.service_user.application.gateway.UserBatchReader;

import java.io.InputStream;
import java.util.List;

public record BatchCreateUserUseCase(UserBatchReader batchReader, CreateUserUseCase createUserUseCase) {

    public BatchResult execute(InputStream inputStream) {

        List<CreateUserCommand> rows = batchReader.read(inputStream);

        int success = 0;
        int failed = 0;

        for (CreateUserCommand row : rows) {
            try {
                CreateUserCommand command = new CreateUserCommand(
                        row.cpf,
                        row.name,
                        row.email,
                        row.passwordHash,
                        row.birthDate
                );

                createUserUseCase.execute(command);
                success++;

            } catch (Exception e) {
                failed++;
            }
        }

        return new BatchResult(success, failed);
    }

}
