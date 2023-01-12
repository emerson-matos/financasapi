package br.com.controle.financeiro.controller.api

import br.com.controle.financeiro.model.entity.BankAccount
import br.com.controle.financeiro.model.entity.Card
import br.com.controle.financeiro.model.entity.Client
import br.com.controle.financeiro.model.entity.Institution
import br.com.controle.financeiro.model.entity.Transaction
import br.com.controle.financeiro.model.entity.UserEntity
import java.util.UUID
import java.util.Currency
import java.time.LocalDateTime
import java.math.BigDecimal

val ow = UserEntity("name", "password", "email@email.com", "id")
val cl = Client("name", ow, UUID.randomUUID())
val ins = Institution("identifier", "name", UUID.randomUUID())
val ba = BankAccount("5432", "543", "8", cl, ins, ow, UUID.randomUUID())
val ca = Card("card", "5432", cl, ins, ow, UUID.randomUUID() )
val tra = Transaction("name", LocalDateTime.now(), BigDecimal(1),Currency.getInstance("BRL"), cl, ba, ca, ow, UUID.randomUUID())

open class BaseModelTemplate(
    val card: Card = ca,
    val client: Client = cl,
    val institution: Institution = ins,
    val transaction: Transaction = tra,
    val bankAccount: BankAccount =ba,
    val owner: UserEntity = ow,
)
