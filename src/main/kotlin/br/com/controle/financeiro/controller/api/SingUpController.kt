package br.com.controle.financeiro.controller.api

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.http.HttpStatus
import javax.transaction.Transactional
import br.com.controle.financeiro.model.repository.UserRepository
import br.com.controle.financeiro.model.entity.UserEntity
import java.util.UUID

@RestController
class SingUpController (
    val userRepository: UserRepository
){
    @PostMapping(path = ["/api/open/signup"])
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    fun signUp(@RequestBody signUp: SignUpData) {
        userRepository.save(UserEntity(UUID.randomUUID().toString(), signUp.name, signUp.password, signUp.email))
    }
}

data class SignUpData(
    val email: String,
    val password: String,
    val name: String,
)
