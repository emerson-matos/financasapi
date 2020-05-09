package br.com.controle.financeiro.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SingUp {
    private static final Logger LOG = LoggerFactory.getLogger(SingUp.class);

    @PostMapping("/signup")
    public void signUp(@RequestHeader String token) {
        LOG.info("SignUp request for ${}", token);
        //TODO(register) 
    }
}