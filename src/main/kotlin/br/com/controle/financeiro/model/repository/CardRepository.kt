package br.com.controle.financeiro.model.repository

import java.util.UUID
import java.util.Optional
import br.com.controle.financeiro.model.entity.Card
import br.com.controle.financeiro.model.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CardRepository : JpaRepository<Card, UUID> {
    fun findAllByOwner(owner: UserEntity): List<Card>
    fun findByIdAndOwner(responsible: UUID, owner: UserEntity): Optional<Card>
}
