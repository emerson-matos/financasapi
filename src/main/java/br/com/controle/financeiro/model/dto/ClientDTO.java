package br.com.controle.financeiro.model.dto;

import java.io.Serializable;

import br.com.controle.financeiro.model.entity.Client;

public class ClientDTO implements Serializable {

	private Long clientId;

	private String name;

	public ClientDTO() {
		super();
	}

	public ClientDTO(final String name) {
		super();
		this.name = name;
	}

	private ClientDTO(Long id, String name) {
		super();
		this.name = name;
		this.clientId = id;
	}

	public static ClientDTO fromClient(Client client) {
		return new ClientDTO(client.getId(), client.getName());
	}

	public Long getId() {
		return this.clientId;
	}

	public void setId(final Long id) {
		this.clientId = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Client toClient() {
		return new Client(this.name, this.clientId);
	}

}
