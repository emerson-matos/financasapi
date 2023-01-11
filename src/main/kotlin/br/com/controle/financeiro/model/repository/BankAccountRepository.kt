package br.com.controle.financeiro.model.repository

import br.com.controle.financeiro.model.entity.BankAccount
import br.com.controle.financeiro.model.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID
import java.util.Optional

interface BankAccountRepository : JpaRepository<BankAccount, UUID> {
    fun findAllByOwner(owner: UserEntity): List<BankAccount>
    fun findByIdAndOwner(id: UUID, owner: UserEntity): Optional<BankAccount>
}
