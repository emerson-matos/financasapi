package br.com.controle.financeiro.model.dto

import java.util.UUID
import java.io.Serializable
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import br.com.controle.financeiro.model.entity.Client
import br.com.controle.financeiro.model.entity.UserEntity

class ClientDTO(
    val id: UUID,
    @NotBlank val name: String,
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    val owner: UserEntity,
) : Serializable {

    fun toClient(owner: UserEntity): Client {
        return Client(id, name, owner)
    }

    companion object {
        fun fromClient(client: Client): ClientDTO {
            return ClientDTO(client.id, client.name, client.owner)
        }
    }
}
