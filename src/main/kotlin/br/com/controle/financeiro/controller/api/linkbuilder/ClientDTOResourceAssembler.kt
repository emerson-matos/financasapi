package br.com.controle.financeiro.controller.api.linkbuilder

import org.springframework.stereotype.Component
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.hateoas.EntityModel
import br.com.controle.financeiro.model.dto.ClientDTO
import br.com.controle.financeiro.controller.api.ClientController

@Component
class ClientDTOResourceAssembler : RepresentationModelAssembler<ClientDTO, EntityModel<ClientDTO>> {
    override fun toModel(entity: ClientDTO): EntityModel<ClientDTO> {
        return EntityModel.of(
            entity, WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(
                    ClientController::class.java
                ).oneClient(entity.id)
            ).withSelfRel(),
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ClientController::class.java).allClients())
                .withRel("clients")
        )
    }
}
