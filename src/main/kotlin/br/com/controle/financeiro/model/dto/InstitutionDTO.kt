package br.com.controle.financeiro.model.dto

import br.com.controle.financeiro.model.entity.Institution
import jakarta.validation.constraints.NotBlank
import java.io.Serializable
import java.util.UUID

data class InstitutionDTO(
        @NotBlank val name: String,
        @NotBlank val identifier: String,
        val id: UUID? = null,
) : Serializable {
    fun toInstitution(): Institution {
        return Institution(
                identifier,
                name,
                id,
        )
    }

    companion object {
        fun fromInstitution(institution: Institution): InstitutionDTO {
            return InstitutionDTO(
                    institution.identifier,
                    institution.name,
                    institution.id,
            )
        }
    }
}
