package br.com.controle.financeiro.model.dto

import br.com.controle.financeiro.model.entity.BankAccount
import br.com.controle.financeiro.model.entity.Client
import br.com.controle.financeiro.model.entity.Institution
import br.com.controle.financeiro.model.entity.UserEntity
import jakarta.validation.constraints.NotBlank
import java.io.Serializable
import java.util.UUID

data class BankAccountDTO(
        @NotBlank val agency: String,
        @NotBlank val number: String,
        val dac: @NotBlank String,
        val responsible: Client,
        val institution: Institution,
        val id: UUID? = null,
) : Serializable {
    fun toBankAccount(
            responsible: Client,
            institution: Institution,
            owner: UserEntity
    ): BankAccount {
        return BankAccount(agency, number, dac, responsible, institution, owner, id)
    }

    companion object {
        fun fromBankAccount(account: BankAccount): BankAccountDTO {
            return BankAccountDTO(
                    account.agency,
                    account.number,
                    account.dac,
                    account.responsible,
                    account.institution,
                    account.id,
            )
        }
    }
}
