package br.com.controle.financeiro.service

import br.com.controle.financeiro.model.entity.UserEntity
import org.springframework.security.core.userdetails.UserDetailsService

interface UserService : UserDetailsService {
    fun registerUser(init: RegisterUserInit): UserEntity?
    val authenticatedUser: UserEntity?

    class RegisterUserInit(val userName: String?, val email: String?, val name: String?)
}