package br.com.controle.financeiro.service.impl;

import br.com.controle.financeiro.configuration.auth.firebase.FirebaseTokenHolder;
import br.com.controle.financeiro.service.FirebaseParserService;
import br.com.controle.financeiro.service.exception.FirebaseTokenInvalidException;
import br.com.controle.financeiro.spring.conditionals.FirebaseCondition;
import com.google.firebase.auth.AbstractFirebaseAuth;
import com.google.firebase.auth.FirebaseToken;

import org.springframework.stereotype.Service;

@Service
@FirebaseCondition
public class FirebaseParserServiceImpl implements FirebaseParserService {

    private final AbstractFirebaseAuth firebaseAuth;

    public FirebaseParserServiceImpl(AbstractFirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public FirebaseTokenHolder parseToken(String idToken) {
        if (idToken.isBlank()) {
            throw new IllegalArgumentException("FirebaseTokenBlank");
        }
        try {
            FirebaseToken customToken = firebaseAuth.verifyIdToken(idToken);

            return new FirebaseTokenHolder(customToken);
        } catch (Exception e) {
            throw new FirebaseTokenInvalidException(e.getMessage());
        }
    }

}