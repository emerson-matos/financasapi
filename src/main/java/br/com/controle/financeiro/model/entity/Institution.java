package br.com.controle.financeiro.model.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Institution implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long institutionId;
	private String name;
	private String identifier;

	public Institution() {
		super();
	}

	public Institution(final String name) {
		super();
		this.name = name;
	}

	public Long getInstitutionId() {
		return institutionId;
	}

	public void setIdInstituicao(final Long institutionId) {
		this.institutionId = institutionId;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(final String identifier) {
		this.identifier = identifier;
	}
}
