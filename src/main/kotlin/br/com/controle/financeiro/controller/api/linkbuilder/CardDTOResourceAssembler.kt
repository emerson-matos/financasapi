package br.com.controle.financeiro.controller.api.linkbuilder

import org.springframework.stereotype.Component
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.hateoas.server.RepresentationModelAssembler
import br.com.controle.financeiro.controller.api.CardController
import br.com.controle.financeiro.model.dto.CardDTO

@Component
class CardDTOResourceAssembler : RepresentationModelAssembler<CardDTO, EntityModel<CardDTO>> {
    override fun toModel(entity: CardDTO): EntityModel<CardDTO> {
        return EntityModel.of(
            entity, WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(
                    CardController::class.java
                ).oneCard(entity.id)
            ).withSelfRel(),
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CardController::class.java).allCards()).withRel("cards")
        )
    }
}
