package br.com.controle.financeiro.model.exception

import java.util.UUID

class ClientNotFoundException(id: UUID) : NotFoundException("Could not find client " + id.toString())
