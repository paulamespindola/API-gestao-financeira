package br.com.gestaofinanceira.service_user.infrastructure.springdoc;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfiguration {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
//                .components(new Components()
//                        .addSecuritySchemes("bearer-key",
//                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .info(new Info()
                        .title("Service User")
                        .description("API de serviço de usuário, contendo as funcionalidades de CRUD usuários")
                        .contact(new Contact()
                                .name("Paula Mirela")
                                .email("paulamespindola@gmail.com")));
    }


}