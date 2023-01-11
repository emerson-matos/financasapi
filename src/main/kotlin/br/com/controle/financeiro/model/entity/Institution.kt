package br.com.controle.financeiro.model.entity

import java.util.UUID
import jakarta.persistence.Id
import jakarta.persistence.Entity

@Entity(name = "institution")
class Institution(
        @Id val id: UUID,
        val name: String,
        val identifier: String,
)
