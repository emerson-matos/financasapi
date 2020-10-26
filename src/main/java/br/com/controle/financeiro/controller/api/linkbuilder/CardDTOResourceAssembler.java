package br.com.controle.financeiro.controller.api.linkbuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import br.com.controle.financeiro.controller.api.CardController;
import br.com.controle.financeiro.model.dto.CardDTO;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class CardDTOResourceAssembler implements RepresentationModelAssembler<CardDTO, EntityModel<CardDTO>> {

    @Override
    public EntityModel<CardDTO> toModel(CardDTO entity) {
        return new EntityModel<>(entity, linkTo(methodOn(CardController.class).oneCard(entity.getId())).withSelfRel(),
                              linkTo(methodOn(CardController.class).allCards()).withRel("cards"));
    }

}
