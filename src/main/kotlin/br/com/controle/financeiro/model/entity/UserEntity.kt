package br.com.controle.financeiro.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.validation.constraints.Email

@Entity(name = "user_entity")
data class UserEntity(
        @Column(name = "name") val name: String,
        @Column(name = "password", nullable = false) private val password: String,
        @Column(name = "email", nullable = false) @Email val email: String,
        @Id @GeneratedValue @Column(name = "id_user") val id: String? = null,
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
