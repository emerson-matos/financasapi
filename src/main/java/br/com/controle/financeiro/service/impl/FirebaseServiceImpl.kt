package br.com.controle.financeiro.service.impl

import br.com.controle.financeiro.configuration.security.auth.firebase.FirebaseTokenHolder
import br.com.controle.financeiro.service.FirebaseParserService
import br.com.controle.financeiro.service.FirebaseService
import br.com.controle.financeiro.spring.conditionals.FirebaseCondition
import org.springframework.stereotype.Service

@Service
@FirebaseCondition
class FirebaseServiceImpl(
    private val parseService: FirebaseParserService
) : FirebaseService {
    override fun parseToken(idToken: String): FirebaseTokenHolder? {
        return parseService.parseToken(idToken)
    }
}