package br.com.controle.financeiro.controller.api;

import javax.transaction.Transactional;

import br.com.controle.financeiro.configuration.auth.firebase.FirebaseTokenHolder;
import br.com.controle.financeiro.service.FirebaseService;
import br.com.controle.financeiro.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SingUp {
    private static final Logger LOG = LoggerFactory.getLogger(SingUp.class);

    @Autowired(required = false)
    private FirebaseService firebaseService;

    @Autowired
    private UserService userService;

    @PostMapping("/open/signup")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void signUp(@RequestHeader(name = "X-Authorization-Firebase") String token) {
        LOG.info("SignUp request for {}", token);

        if (token.isBlank()) {
            throw new IllegalArgumentException("FirebaseTokenBlank");
        }
        FirebaseTokenHolder tokenHolder = firebaseService.parseToken(token);
        userService.registerUser(new UserService.RegisterUserInit(tokenHolder.getUid(), tokenHolder.getEmail()));
    }
}