package br.com.controle.financeiro.service;

import br.com.controle.financeiro.configuration.auth.firebase.FirebaseTokenHolder;

public interface FirebaseService {

    FirebaseTokenHolder parseToken(String idToken);

}