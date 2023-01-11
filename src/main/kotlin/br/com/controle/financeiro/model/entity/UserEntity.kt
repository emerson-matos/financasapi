package br.com.controle.financeiro.model.entity

import jakarta.validation.constraints.Email
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Column

@Entity(name = "user_entity")
class UserEntity(
        @Id @Column(name = "id_user") val id: String,
        @Column(name = "name") val name: String,
        @Column(name = "password", nullable = false) private val password: String,
        @Column(name = "email", nullable = false) val email: @Email String,
) {

    fun isAccountNonExpired(): Boolean {
        return true
    }

    fun isAccountNonLocked(): Boolean {
        return true
    }

    fun isCredentialsNonExpired(): Boolean {
        return true
    }

    fun isEnabled(): Boolean {
        return true
    }
}
