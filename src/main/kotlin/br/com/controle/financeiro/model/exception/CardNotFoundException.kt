package br.com.controle.financeiro.model.exception

import java.util.UUID

class CardNotFoundException(id: UUID) : NotFoundException("Could not find card $id")
