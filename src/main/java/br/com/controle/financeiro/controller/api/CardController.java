package br.com.controle.financeiro.controller.api;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import br.com.controle.financeiro.controller.api.linkbuilder.CardDTOResourceAssembler;
import br.com.controle.financeiro.model.dto.CardDTO;
import br.com.controle.financeiro.model.entity.Card;
import br.com.controle.financeiro.model.entity.Client;
import br.com.controle.financeiro.model.entity.Institution;
import br.com.controle.financeiro.model.exception.CardNotFoundException;
import br.com.controle.financeiro.model.repository.CardRepository;
import br.com.controle.financeiro.model.repository.ClientRepository;
import br.com.controle.financeiro.model.repository.InstitutionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/card", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class CardController {

    private static final Logger LOG = LoggerFactory.getLogger(CardController.class);

    private final CardRepository cardRepository;
    private final CardDTOResourceAssembler cardDTOResourceAssembler;
    private final ClientRepository clientRepository;
    private final InstitutionRepository institutionRepository;

    public CardController(final CardRepository cardRepository, final CardDTOResourceAssembler cardDTOResourceAssembler,
                          final ClientRepository clientRepository, final InstitutionRepository institutionRepository) {
        this.cardRepository = cardRepository;
        this.cardDTOResourceAssembler = cardDTOResourceAssembler;
        this.clientRepository = clientRepository;
        this.institutionRepository = institutionRepository;
    }

    @GetMapping
    public Resources<Resource<CardDTO>> allCards() {
        LOG.debug("finding allCards");

        final List<Resource<CardDTO>> cards =
                cardRepository.findAll().stream().map(CardDTO::fromCard).map(cardDTOResourceAssembler::toResource)
                              .collect(Collectors.toList());

        return new Resources<>(cards, linkTo(methodOn(CardController.class).allCards()).withSelfRel());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    public Resource<CardDTO> newCard(@RequestBody @Valid final CardDTO card) {
        LOG.debug("creating newCard");
        //TODO extract to service
        Optional<Client> c = clientRepository.findById(card.getClient());
        Optional<Institution> i = institutionRepository.findById(card.getInstitution());
        CardDTO savedCard = CardDTO.fromCard(cardRepository.save(card.toCard(c.get(), i.get())));
        return cardDTOResourceAssembler.toResource(savedCard);
    }

    @GetMapping(path = "/{id}")
    public Resource<CardDTO> oneCard(@PathVariable(value = "id") final long id) {
        LOG.debug("searching oneCard ${}", id);
        final Card savedCard = cardRepository.findById(id).orElseThrow(() -> new CardNotFoundException(id));
        return cardDTOResourceAssembler.toResource(CardDTO.fromCard(savedCard));
    }

    @PutMapping(path = "/{id}")
    public Resource<CardDTO> replaceCard(@RequestBody final CardDTO newCard, @PathVariable final Long id) {
        LOG.info("replaceCard");
        //TODO verify DTO integrity
        Card savedCard = cardRepository.findById(id).map(card -> {
            card.setName(newCard.getName());
            return cardRepository.save(card);
        }).orElseGet(() -> {
            //TODO extract to service
            Optional<Client> c = clientRepository.findById(newCard.getClient());
            Optional<Institution> i = institutionRepository.findById(newCard.getInstitution());
            newCard.setId(id);
            return cardRepository.save(newCard.toCard(c.get(), i.get()));
        });

        return cardDTOResourceAssembler.toResource(CardDTO.fromCard(savedCard));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{id}")
    public void deleteCard(@PathVariable final Long id) {
        LOG.debug("trying to deleteCard ${}", id);
        cardRepository.deleteById(id);
    }

}
