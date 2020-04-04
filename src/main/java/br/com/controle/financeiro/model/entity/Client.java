package br.com.controle.financeiro.model.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Client implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long clientId;

	private String name;

	public Client() {
		super();
	}

	public Client(final String name) {
		super();
		this.name = name;
	}

	public Client(String name, Long id) {
		this.name = name;
		this.clientId = id;
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

}
