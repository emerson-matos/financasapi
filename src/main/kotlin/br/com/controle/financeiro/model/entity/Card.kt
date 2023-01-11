package br.com.controle.financeiro.model.entity

import java.io.Serializable
import java.util.UUID
import org.springframework.data.jpa.domain.AbstractPersistable
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.JoinColumn
import jakarta.persistence.FetchType
import jakarta.persistence.CascadeType

@Entity(name = "card")
class Card(
    @Id val id: UUID,
    val name: String,
    val number: String,
    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.DETACH])
    @JoinColumn(name = "id_client")
    val responsible: Client,
    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.DETACH])
    @JoinColumn(name = "id_institution")
    val institution: Institution,
    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.DETACH])
    @JoinColumn(name = "id_user")
    val owner: UserEntity,
) : AbstractPersistable<UUID>(), Serializable
