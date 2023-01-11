package br.com.controle.financeiro.model.dto

import br.com.controle.financeiro.model.entity.Institution
import java.io.Serializable
import java.util.UUID
import jakarta.validation.constraints.NotBlank

class InstitutionDTO(
        val id: UUID = UUID.randomUUID(),
        @NotBlank val name: String,
        @NotBlank val identifier: String,
) : Serializable {
    fun toInstitution(): Institution {
        return Institution(id, identifier, name)
    }

    companion object {
        fun fromInstitution(institution: Institution): InstitutionDTO {
            return InstitutionDTO(institution.id, institution.identifier, institution.name)
        }
    }
}
