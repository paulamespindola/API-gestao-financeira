package br.com.gestaofinanceira.service_user.config;

import br.com.gestaofinanceira.service_user.application.gateway.UserBatchReader;
import br.com.gestaofinanceira.service_user.application.gateway.UserRepository;
import br.com.gestaofinanceira.service_user.application.use_cases.*;
import br.com.gestaofinanceira.service_user.infrastructure.gateway.ApachePoiUserBatchReader;
import br.com.gestaofinanceira.service_user.infrastructure.gateway.UserEntityMapper;
import br.com.gestaofinanceira.service_user.infrastructure.gateway.UserRepositoryAdapter;
import br.com.gestaofinanceira.service_user.infrastructure.persistence.UserRepositoryJpa;
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class UserConfig {

    @Bean
    public UserRepository userRepository(UserRepositoryJpa jpaUserRepository, UserEntityMapper userEntityMapper) {
        return new UserRepositoryAdapter(jpaUserRepository, userEntityMapper);
    }

    @Bean
    public UserBatchReader userBatchReader() {
        return new ApachePoiUserBatchReader();
    }

    @Bean
    public BatchCreateUserUseCase batchCreateUserUseCase(
            UserBatchReader userBatchReader,
            CreateUserUseCase createUserUseCase
    ) {
        return new BatchCreateUserUseCase(userBatchReader, createUserUseCase);
    }

    @Bean
    public CreateUserUseCase createUserUseCase(UserRepository userRepository){
        return new CreateUserUseCase(userRepository);
    }

    @Bean
    public DeleteUserUseCase deleteUserUseCase(UserRepository userRepository){
        return new DeleteUserUseCase(userRepository);
    }

    @Bean
    public GetUserUseCase getUserUseCase(UserRepository userRepository){
        return new GetUserUseCase(userRepository);
    }

    @Bean
    public ListUsersUseCase listUsersUseCase(UserRepository userRepository){
        return new ListUsersUseCase(userRepository);
    }

    @Bean UpdateUserUseCase updateUserUseCase(UserRepository userRepository){
        return new UpdateUserUseCase(userRepository);
    }

    @Bean
    public UserEntityMapper userEntityMapper(){
        return new UserEntityMapper();
    }
}
