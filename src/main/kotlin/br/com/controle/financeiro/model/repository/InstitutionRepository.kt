package br.com.controle.financeiro.model.repository

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import br.com.controle.financeiro.model.entity.Institution

interface InstitutionRepository : JpaRepository<Institution, UUID>
