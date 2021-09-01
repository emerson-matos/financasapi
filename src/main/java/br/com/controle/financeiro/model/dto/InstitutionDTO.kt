package br.com.controle.financeiro.model.dto

import br.com.controle.financeiro.model.entity.Institution
import java.io.Serializable
import java.util.*
import javax.validation.constraints.NotBlank

class InstitutionDTO : Serializable {
    var id: UUID? = null
    var name: @NotBlank String? = null
    var identifier: @NotBlank String? = null

    constructor() : super() {}
    constructor(id: UUID?, identifier: String?, name: String?) : super() {
        this.id = id
        this.identifier = identifier
        this.name = name
    }

    fun toInstitution(): Institution {
        return Institution(id, identifier, name)
    }

    companion object {
        fun fromInstitution(institution: Institution?): InstitutionDTO {
            return InstitutionDTO(institution?.id, institution?.identifier, institution?.name)
        }
    }
}