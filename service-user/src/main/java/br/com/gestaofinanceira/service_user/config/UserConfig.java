package br.com.gestaofinanceira.service_user.config;

import br.com.gestaofinanceira.service_user.application.gateway.UserBatchReader;
import br.com.gestaofinanceira.service_user.application.gateway.UserRepository;
import br.com.gestaofinanceira.service_user.application.port.PasswordHasher;
import br.com.gestaofinanceira.service_user.application.port.PasswordMatcher;
import br.com.gestaofinanceira.service_user.application.port.TokenGenerator;
import br.com.gestaofinanceira.service_user.application.port.TokenVerifier;
import br.com.gestaofinanceira.service_user.application.use_cases.*;
import br.com.gestaofinanceira.service_user.infrastructure.gateway.ApachePoiUserBatchReader;
import br.com.gestaofinanceira.service_user.infrastructure.gateway.UserEntityMapper;
import br.com.gestaofinanceira.service_user.infrastructure.gateway.UserRepositoryAdapter;
import br.com.gestaofinanceira.service_user.infrastructure.persistence.UserRepositoryJpa;
import br.com.gestaofinanceira.service_user.infrastructure.security.BCryptPasswordHasher;
import br.com.gestaofinanceira.service_user.infrastructure.security.UserDetailsServiceImpl;
import br.com.gestaofinanceira.service_user.infrastructure.security.filter.JwtAuthenticationFilter;
import br.com.gestaofinanceira.service_user.infrastructure.security.jwt.JwtTokenGenerator;
import br.com.gestaofinanceira.service_user.infrastructure.security.jwt.JwtTokenVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class UserConfig {

//    @Bean
//    public UserRepository userRepository(UserRepositoryJpa jpaUserRepository, UserEntityMapper userEntityMapper) {
//        return new UserRepositoryAdapter(jpaUserRepository, userEntityMapper);
//    }

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
    public CreateUserUseCase createUserUseCase(UserRepository userRepository, PasswordHasher passwordHasher){
        return new CreateUserUseCase(userRepository, passwordHasher);
    }

    @Bean
    public PasswordHasher passwordHasher(){
        return new BCryptPasswordHasher();
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

    @Bean
    public AuthenticateUser authenticateUser(UserRepository repository,
                                             PasswordMatcher passwordMatcher,
                                             TokenGenerator tokenGenerator){
        return new AuthenticateUser(repository, passwordMatcher, tokenGenerator);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(TokenVerifier verifier, UserDetailsServiceImpl userDetailsService){
        return new JwtAuthenticationFilter(verifier, userDetailsService);
    }

    @Bean
    public TokenVerifier tokenVerifier(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.issuer:service-user}") String issuer
    ) {
        return new JwtTokenVerifier(secret, issuer);
    }

    @Bean
    public TokenGenerator tokenGenerator(){
        return new JwtTokenGenerator();
    }

    @Bean
    public UserDetailsServiceImpl userDetailsService(UserRepository userRepository) {
        return new UserDetailsServiceImpl(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
