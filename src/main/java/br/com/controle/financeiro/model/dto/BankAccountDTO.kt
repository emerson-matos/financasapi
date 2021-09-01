package br.com.controle.financeiro.model.dto

import br.com.controle.financeiro.model.entity.BankAccount
import br.com.controle.financeiro.model.entity.Client
import br.com.controle.financeiro.model.entity.Institution
import br.com.controle.financeiro.model.entity.UserEntity
import java.io.Serializable
import java.util.*
import javax.validation.constraints.NotBlank

class BankAccountDTO : Serializable {
    var id: UUID? = null
    var agency: @NotBlank String? = null
    var number: @NotBlank String? = null
    var dac: @NotBlank String? = null
    var responsible: UUID? = UUID.randomUUID()
        private set
    var institution: UUID? = UUID.randomUUID()

    constructor() : super() {}
    constructor(
        id: UUID?, agency: String?, number: String?, dac: String?, responsible: UUID?,
        institution: UUID?
    ) : super() {
        this.id = id
        this.agency = agency
        this.number = number
        this.dac = dac
        this.responsible = responsible
        this.institution = institution
    }

    fun toBankAccount(responsible: Client?, institution: Institution?, owner: UserEntity?): BankAccount {
        return BankAccount(id, agency, number, dac, responsible, institution, owner)
    }

    fun setOwner(owner: UUID?) {
        responsible = owner
    }

    companion object {
        fun fromBankAccount(account: BankAccount?): BankAccountDTO {
            return BankAccountDTO(
                account!!.id, account.agency, account.number, account.dac,
                account.responsible?.id, account.institution?.id
            )
        }
    }
}