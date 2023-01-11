package br.com.controle.financeiro.model.exception

import java.util.UUID

class BankAccountNotFoundException(id: UUID) : NotFoundException("Could not find BankAccount $id")
