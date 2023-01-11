package br.com.controle.financeiro.model.dto

import br.com.controle.financeiro.model.entity.BankAccount
import br.com.controle.financeiro.model.entity.Client
import br.com.controle.financeiro.model.entity.Institution
import br.com.controle.financeiro.model.entity.UserEntity
import jakarta.validation.constraints.NotBlank
import java.io.Serializable
import java.util.UUID

class BankAccountDTO(
        val id: UUID,
        @NotBlank val agency: String,
        @NotBlank val number: String,
        val dac: @NotBlank String,
        val responsible: Client,
        val institution: Institution,
) : Serializable {
    fun toBankAccount(
            responsible: Client,
            institution: Institution,
            owner: UserEntity
    ): BankAccount {
        return BankAccount(id, agency, number, dac, responsible, institution, owner)
    }

    companion object {
        fun fromBankAccount(account: BankAccount): BankAccountDTO {
            return BankAccountDTO(
                    account.id,
                    account.agency,
                    account.number,
                    account.dac,
                    account.responsible,
                    account.institution
            )
        }
    }
}
