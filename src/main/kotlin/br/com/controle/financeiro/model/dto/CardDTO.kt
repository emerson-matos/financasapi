package br.com.controle.financeiro.model.dto

import br.com.controle.financeiro.model.entity.Card
import br.com.controle.financeiro.model.entity.Client
import br.com.controle.financeiro.model.entity.Institution
import br.com.controle.financeiro.model.entity.UserEntity
import jakarta.validation.constraints.NotBlank
import java.util.UUID

data class CardDTO(
        @NotBlank val name: String,
        @NotBlank val number: String,
        val client: Client,
        val institution: Institution,
        val id: UUID? = null,
) {

    fun toCard(client: Client, institution: Institution, owner: UserEntity): Card {
        return Card(name, number, client, institution, owner, id)
    }

    companion object {
        fun fromCard(card: Card): CardDTO {
            return CardDTO(
                    card.name,
                    card.number,
                    card.responsible,
                    card.institution,
                    card.id,
            )
        }
    }
}
