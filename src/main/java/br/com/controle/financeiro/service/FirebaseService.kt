package br.com.controle.financeiro.service

import br.com.controle.financeiro.configuration.security.auth.firebase.FirebaseTokenHolder

interface FirebaseService {
    fun parseToken(idToken: String): FirebaseTokenHolder?
}