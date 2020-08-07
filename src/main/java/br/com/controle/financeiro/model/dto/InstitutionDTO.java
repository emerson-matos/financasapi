package br.com.controle.financeiro.model.dto;

import java.io.Serializable;
import java.util.UUID;

import javax.validation.constraints.NotBlank;

import br.com.controle.financeiro.model.entity.Institution;

public class InstitutionDTO implements Serializable {

    private UUID institutionId;

    @NotBlank
    private String name;

    @NotBlank
    private String identifier;

    public InstitutionDTO() {
        super();
    }

    public InstitutionDTO(final UUID id, final String identifier, final String name) {
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

    public UUID getId() {
        return institutionId;
    }

    public void setId(final UUID institutionId) {
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