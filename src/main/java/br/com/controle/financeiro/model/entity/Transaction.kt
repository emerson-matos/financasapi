package br.com.controle.financeiro.model.entity

import org.springframework.data.jpa.domain.AbstractPersistable
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity(name = "transaction")
class Transaction : AbstractPersistable<UUID?>, Serializable {
    var name: String? = null
    var transactionDate: LocalDateTime? = null
    var value: BigDecimal? = null
    var currency: Currency? = null

    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.DETACH])
    @JoinColumn(name = "id_client")
    var responsible: Client? = null

    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.DETACH])
    @JoinColumn(name = "id_account")
    var bankAccount: BankAccount? = null

    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.DETACH])
    @JoinColumn(name = "id_card")
    var card: Card? = null

    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.DETACH])
    @JoinColumn(name = "id_user")
    private var owner: UserEntity? = null

    constructor() : super() {}
    constructor(
        id: UUID?, name: String?, transactionDate: LocalDateTime?, value: BigDecimal?, currency: Currency?,
        responsible: Client?, account: BankAccount?, card: Card?, owner: UserEntity?
    ) : super() {
        this.id = id
        this.name = name
        this.transactionDate = transactionDate
        this.value = value
        this.currency = currency
        this.responsible = responsible
        bankAccount = account
        this.card = card
        this.owner = owner
    }
}