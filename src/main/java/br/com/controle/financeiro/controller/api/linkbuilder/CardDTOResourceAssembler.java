package br.com.controle.financeiro.controller.api.linkbuilder;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import br.com.controle.financeiro.controller.api.CardController;
import br.com.controle.financeiro.model.dto.CardDTO;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class CardDTOResourceAssembler implements ResourceAssembler<CardDTO, Resource<CardDTO>> {

    @Override
    public Resource<CardDTO> toResource(CardDTO entity) {
        return new Resource<>(entity, linkTo(methodOn(CardController.class).oneCard(entity.getId())).withSelfRel(),
                              linkTo(methodOn(CardController.class).allCards()).withRel("cards"));
    }

}
