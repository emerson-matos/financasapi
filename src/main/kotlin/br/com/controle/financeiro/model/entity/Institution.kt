package br.com.controle.financeiro.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.util.UUID

@Entity(name = "institution")
data class Institution(
                val name: String,
                val identifier: String,
                @Id @GeneratedValue val id: UUID? = null,
)
