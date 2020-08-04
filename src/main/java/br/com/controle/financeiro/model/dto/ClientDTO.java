package br.com.controle.financeiro.model.dto;

import java.io.Serializable;
import java.util.UUID;

import javax.validation.constraints.NotBlank;

import br.com.controle.financeiro.model.entity.Client;
import br.com.controle.financeiro.model.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientDTO implements Serializable {

    @NotBlank
    private String name;

    private UUID client;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String owner;

    public ClientDTO() {
        super();
    }

    private ClientDTO(final UUID id, final String name, final String owner) {
        super();
        this.client = id;
        this.name = name;
        this.owner = owner;
    }

    public static ClientDTO fromClient(Client client) {
        return new ClientDTO(client.getId(), client.getName(), client.getOwner().getUsername());
    }

    public Client toClient(UserEntity owner) {
        return new Client(this.client, this.name, owner);
    }

    public UUID getId() {
        return this.client;
    }

    public void setId(final UUID id) {
        this.client = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

}
