package br.com.controle.financeiro.model.dto

import br.com.controle.financeiro.model.entity.Card
import br.com.controle.financeiro.model.entity.Client
import br.com.controle.financeiro.model.entity.Institution
import br.com.controle.financeiro.model.entity.UserEntity
import jakarta.validation.constraints.NotBlank
import java.util.UUID

class CardDTO(
        val id: UUID,
        @NotBlank val name: String,
        @NotBlank val number: String,
        val client: Client,
        val institution: Institution,
) {

    fun toCard(client: Client, institution: Institution, owner: UserEntity): Card {
        return Card(id, name, number, client, institution, owner)
    }

    companion object {
        fun fromCard(card: Card): CardDTO {
            return CardDTO(
                    card.id,
                    card.name,
                    card.number,
                    card.responsible,
                    card.institution
            )
        }
    }
}
