package br.com.controle.financeiro.model.entity

import java.util.UUID
import java.io.Serializable
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.JoinColumn
import jakarta.persistence.FetchType
import jakarta.persistence.CascadeType

@Entity(name = "client")
class Client(
    @Id val id: UUID,
    val name: String,
    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.DETACH])
    @JoinColumn(name = "id_user")
    val owner: UserEntity,
)
