package br.com.gestaofinanceira.service_user.infrastructure.controller;

import br.com.gestaofinanceira.service_user.application.command.LoginCommand;
import br.com.gestaofinanceira.service_user.application.port.AuthResult;
import br.com.gestaofinanceira.service_user.application.use_cases.AuthenticateUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticateUser authenticateUser;

    public AuthController(AuthenticateUser authenticateUser) {
        this.authenticateUser = authenticateUser;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResult> login(@RequestBody LoginCommand command) {
        AuthResult result = authenticateUser.execute(command);
        return ResponseEntity.ok(result);
    }
}
