package br.com.controle.financeiro.model.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.util.UUID

@Entity(name = "client")
data class Client(
        val name: String,
        @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.DETACH])
        @JoinColumn(name = "id_user")
        val owner: UserEntity,
        @Id @GeneratedValue val id: UUID? = null,
)
