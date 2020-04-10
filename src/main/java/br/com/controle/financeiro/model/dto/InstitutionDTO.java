package br.com.controle.financeiro.model.dto;

import java.io.Serializable;

import br.com.controle.financeiro.model.entity.Institution;

public class InstitutionDTO implements Serializable {

	private Long institutionId;
	private String name;
	private String identifier;

	public InstitutionDTO() {
		super();
	}

	public InstitutionDTO(final Long id, final String identifier, final String name) {
		super();
		this.institutionId = id;
		this.identifier = identifier;
		this.name = name;
	}

	public static InstitutionDTO fromInstitution(Institution institution) {
		return new InstitutionDTO(institution.getId(), institution.getIdentifier(), institution.getName());
	}

	public Institution toInstitution() {
		return new Institution(this.institutionId, this.identifier, this.name);
	}

	public Long getId() {
		return institutionId;
	}

	public void setId(final Long institutionId) {
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