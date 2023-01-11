package br.com.controle.financeiro.model.exception

import java.util.UUID

class InstitutionNotFoundException(id: UUID) : NotFoundException("Could not find institution $id")
