package br.com.controle.financeiro.controller.api;

import javax.transaction.Transactional;

import br.com.controle.financeiro.configuration.auth.firebase.FirebaseTokenHolder;
import br.com.controle.financeiro.service.FirebaseService;
import br.com.controle.financeiro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SingUp {
    @Autowired(required = false)
    private FirebaseService firebaseService;

    @Autowired
    private UserService userService;

    @PostMapping(path = "/open/signup")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void signUp(@RequestHeader(name = "X-Firebase-User") String token) {
        if (token.isBlank()) {
            throw new IllegalArgumentException("FirebaseTokenBlank");
        }
        FirebaseTokenHolder tokenHolder = firebaseService.parseToken(token);
        userService.registerUser(new UserService.RegisterUserInit(tokenHolder.getUid(), tokenHolder.getEmail()));
    }

}