package br.com.controle.financeiro.controller.linkbuilder;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import br.com.controle.financeiro.controller.CardController;
import br.com.controle.financeiro.model.entity.Card;

@Component
public class CardResourceAssembler implements ResourceAssembler<Card, Resource<Card>> {

	@Override
	public Resource<Card> toResource(Card entity) {
		return new Resource<>(entity, linkTo(methodOn(CardController.class).oneCard(entity.getId())).withSelfRel(),
				linkTo(methodOn(CardController.class).allCards()).withRel("cards"));
	}

}
