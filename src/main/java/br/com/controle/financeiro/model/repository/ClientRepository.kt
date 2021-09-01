package br.com.controle.financeiro.model.repository

import br.com.controle.financeiro.model.entity.Client
import br.com.controle.financeiro.model.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ClientRepository : JpaRepository<Client?, UUID?> {
    fun findAllByOwner(owner: UserEntity?): List<Client?>
    fun findByIdAndOwner(responsible: UUID?, owner: UserEntity?): Optional<Client?>
}