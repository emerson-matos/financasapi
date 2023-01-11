package br.com.controle.financeiro.model.exception

import java.util.UUID

class TransactionNotFoundException(id: UUID) : NotFoundException("Could not find transaction $id")
