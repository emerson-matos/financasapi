package br.com.controle.financeiro.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.controle.financeiro.controller.linkbuilder.CardResourceAssembler;
import br.com.controle.financeiro.model.entity.Card;
import br.com.controle.financeiro.model.exception.CardNotFoundException;
import br.com.controle.financeiro.model.repository.CardRepository;

@RestController
@RequestMapping("/card")
public class CardController {

	private static final Logger LOG = LoggerFactory.getLogger(CardController.class);

	private final CardRepository cardRepository;

	private final CardResourceAssembler cardResourceAssembler;

	public CardController(final CardRepository cardRepository,
			final CardResourceAssembler cardResourceAssembler) {
		this.cardRepository = cardRepository;
		this.cardResourceAssembler = cardResourceAssembler;
	}

	@GetMapping
	public Resources<Resource<Card>> allCards() {
		LOG.debug("finding allCards");

		final List<Resource<Card>> cards = cardRepository.findAll().stream()
				.map(cardResourceAssembler::toResource).collect(Collectors.toList());

		return new Resources<>(cards, linkTo(methodOn(CardController.class).allCards()).withSelfRel());
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public Resource<Card> newCard(@RequestBody final Card card) {
		LOG.debug("creating newCard");
		return cardResourceAssembler.toResource(cardRepository.save(card));
	}

	@GetMapping(path = "/{id}")
	public Resource<Card> oneCard(@PathVariable(value = "id") final long id) {
		LOG.debug("searching oneCard ${}", id);
		final Card c = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
		return cardResourceAssembler.toResource(c);
	}

	@PutMapping(path = "/{id}")
	public Card replaceCard(@RequestBody final Card newCard, @PathVariable final Long id) {
		LOG.info("replaceCard");
		return cardRepository.findById(id).map(card -> {
			card.setName(newCard.getName());
			return cardRepository.save(card);
		}).orElseGet(() -> {
			newCard.setId(id);
			return cardRepository.save(newCard);
		});
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(path = "/{id}")
	public void deleteCard(@PathVariable final Long id) {
		LOG.debug("trying to deleteCard ${}", id);
		cardRepository.deleteById(id);
	}
}
