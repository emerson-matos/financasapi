package br.com.controle.financeiro.model.dto

import br.com.controle.financeiro.model.entity.BankAccount
import br.com.controle.financeiro.model.entity.Card
import br.com.controle.financeiro.model.entity.Client
import br.com.controle.financeiro.model.entity.Transaction
import br.com.controle.financeiro.model.entity.UserEntity
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import jakarta.validation.constraints.NotBlank
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.Currency
import java.util.Locale
import java.util.UUID

data class TransactionDTO(
        @NotBlank val name: String,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        @JsonDeserialize(using = LocalDateTimeDeserializer::class)
        val transactionDate: LocalDateTime = LocalDateTime.now(),
        val value: BigDecimal = BigDecimal.ZERO,
        val currency: Currency = Currency.getInstance(Locale("pt", "BR")),
        val client: UUID? = null,
        val account: UUID? = null,
        val card: UUID? = null,
        val id: UUID? = null,
) : Serializable {
    fun toTransaction(
            client: Client,
            accountObj: BankAccount,
            cardObj: Card,
            owner: UserEntity
    ): Transaction {
        return Transaction(
                name,
                transactionDate,
                value,
                currency,
                client,
                accountObj,
                cardObj,
                owner,
                id,
        )
    }

    companion object {
        fun fromTransaction(t: Transaction): TransactionDTO {
            return TransactionDTO(
                    t.name,
                    t.transactionDate,
                    t.value,
                    t.currency,
                    t.responsible.id,
                    t.bankAccount.id,
                    t.card.id,
                    t.id,
            )
        }
    }
}
