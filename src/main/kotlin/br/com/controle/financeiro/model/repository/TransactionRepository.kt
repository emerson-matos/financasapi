package br.com.controle.financeiro.model.repository

import java.util.UUID
import java.util.Optional
import br.com.controle.financeiro.model.entity.UserEntity
import br.com.controle.financeiro.model.entity.Transaction
import org.springframework.data.jpa.repository.JpaRepository

interface TransactionRepository : JpaRepository<Transaction, UUID> {
    fun findAllByOwner(owner: UserEntity): List<Transaction>
    fun findByIdAndOwner(responsible: UUID, owner: UserEntity): Optional<Transaction>
}
