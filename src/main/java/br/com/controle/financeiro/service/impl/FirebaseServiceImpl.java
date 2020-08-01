package br.com.controle.financeiro.service.impl;

import br.com.controle.financeiro.configuration.security.auth.firebase.FirebaseTokenHolder;
import br.com.controle.financeiro.service.FirebaseParserService;
import br.com.controle.financeiro.service.FirebaseService;
import br.com.controle.financeiro.spring.conditionals.FirebaseCondition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@FirebaseCondition
public class FirebaseServiceImpl implements FirebaseService {

    @Autowired
    private FirebaseParserService parseService;

    @Override
    public FirebaseTokenHolder parseToken(String firebaseToken) {
        return parseService.parseToken(firebaseToken);
    }

}