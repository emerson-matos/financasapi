package br.com.controle.financeiro.model.dto;

import java.io.Serializable;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.com.controle.financeiro.model.entity.Client;
import br.com.controle.financeiro.model.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientDTO implements Serializable {

    @NotBlank
    private String name;

    @NotNull
    private UUID client;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long owner;

    public ClientDTO() {
        super();
    }

    private ClientDTO(final UUID id, final String name, final Long owner) {
        super();
        this.client = id;
        this.name = name;
        this.owner = owner;
    }

    public static ClientDTO fromClient(Client client) {
        return new ClientDTO(client.getId(), client.getName(), client.getOwner().getId());
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

    public Long getOwner() {
        return owner;
    }

    public void setOwner(Long owner) {
        this.owner = owner;
    }

}
