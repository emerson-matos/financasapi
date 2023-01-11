package br.com.controle.financeiro.model.entity

import java.util.UUID
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.FetchType
import jakarta.persistence.CascadeType

@Entity(name = "bank_account")
class BankAccount(
    @Id val id : UUID,
    val agency: String,
    val number: String,
    val dac: String,
    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.DETACH])
    @JoinColumn(name = "id_client")
    val responsible: Client,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_institution")
    val institution: Institution,
    @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.DETACH])
    @JoinColumn(name = "id_user")
    val owner: UserEntity,
)
