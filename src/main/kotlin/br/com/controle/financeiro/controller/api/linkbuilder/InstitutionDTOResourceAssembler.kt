package br.com.controle.financeiro.controller.api.linkbuilder

import br.com.controle.financeiro.controller.api.InstitutionController
import br.com.controle.financeiro.model.dto.InstitutionDTO
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.stereotype.Component

@Component
class InstitutionDTOResourceAssembler :
        RepresentationModelAssembler<InstitutionDTO, EntityModel<InstitutionDTO>> {
    override fun toModel(entity: InstitutionDTO): EntityModel<InstitutionDTO> {
        return EntityModel.of(
                entity,
                WebMvcLinkBuilder.linkTo(
                                WebMvcLinkBuilder.methodOn(InstitutionController::class.java)
                                        .oneInstitution(entity.id!!)
                        )
                        .withSelfRel(),
                WebMvcLinkBuilder.linkTo(
                                WebMvcLinkBuilder.methodOn(InstitutionController::class.java)
                                        .allInstitutions()
                        )
                        .withRel("institutions")
        )
    }
}
