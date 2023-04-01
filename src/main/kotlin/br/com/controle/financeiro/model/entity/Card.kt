package br.com.controle.financeiro.model.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.io.Serializable
import java.util.UUID
import org.springframework.data.jpa.domain.AbstractPersistable

@Entity(name = "card")
data class Card(
                val name: String,
                val number: String,
                @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.DETACH])
                @JoinColumn(name = "id_institution")
                val institution: Institution,
                @Id @GeneratedValue val id: UUID? = null,
) : AbstractPersistable<UUID>(), Serializable
