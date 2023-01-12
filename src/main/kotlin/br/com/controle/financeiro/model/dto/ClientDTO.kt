package br.com.controle.financeiro.model.dto

import br.com.controle.financeiro.model.entity.Client
import br.com.controle.financeiro.model.entity.UserEntity
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import java.io.Serializable
import java.util.UUID

data class ClientDTO(
        @NotBlank val name: String,
        @JsonProperty(access = JsonProperty.Access.READ_ONLY) val owner: UserEntity,
        val id: UUID? = null,
) : Serializable {

    fun toClient(owner: UserEntity): Client {
        return Client(name, owner, id)
    }

    companion object {
        fun fromClient(client: Client): ClientDTO {
            return ClientDTO(client.name, client.owner, client.id)
        }
    }
}
