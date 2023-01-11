package br.com.controle.financeiro.model.repository

import java.util.UUID
import org.springframework.data.repository.CrudRepository
import br.com.controle.financeiro.model.entity.UserEntity

interface UserRepository : CrudRepository<UserEntity, String>
