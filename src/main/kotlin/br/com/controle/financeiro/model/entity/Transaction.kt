package br.com.controle.financeiro.model.entity

import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID
import java.util.Currency
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.CascadeType

@Entity(name = "transaction")
class Transaction(
    @Id val id: UUID,
    val name: String,
    val transactionDate: LocalDateTime,
    val value: BigDecimal,
    val currency: Currency,
    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.DETACH])
    @JoinColumn(name = "id_client")
    val responsible: Client,
    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.DETACH])
    @JoinColumn(name = "id_account")
    val bankAccount: BankAccount,
    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.DETACH])
    @JoinColumn(name = "id_card")
    val card: Card,
    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.DETACH])
    @JoinColumn(name = "id_user")
    private var owner: UserEntity,
)
