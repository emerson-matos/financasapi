package br.com.controle.financeiro.model.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.Currency
import java.util.UUID

@Entity(name = "transaction")
data class Transaction(
                val name: String,
                val transactionDate: LocalDateTime,
                val value: BigDecimal,
                val currency: Currency,
                @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.DETACH])
                @JoinColumn(name = "id_account")
                val bankAccount: BankAccount,
                @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.DETACH])
                @JoinColumn(name = "id_card")
                val card: Card,
                @Id @GeneratedValue val id: UUID? = null,
)
