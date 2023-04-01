package br.com.controle.financeiro.model.repository

import br.com.controle.financeiro.model.entity.Institution
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource interface InstitutionRepository : JpaRepository<Institution, UUID>
