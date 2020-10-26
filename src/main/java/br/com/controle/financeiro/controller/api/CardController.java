package br.com.controle.financeiro.controller.api;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import br.com.controle.financeiro.controller.api.linkbuilder.CardDTOResourceAssembler;
import br.com.controle.financeiro.model.dto.CardDTO;
import br.com.controle.financeiro.model.entity.Card;
import br.com.controle.financeiro.model.entity.Client;
import br.com.controle.financeiro.model.entity.Institution;
import br.com.controle.financeiro.model.entity.UserEntity;
import br.com.controle.financeiro.model.exception.CardNotFoundException;
import br.com.controle.financeiro.model.repository.CardRepository;
import br.com.controle.financeiro.model.repository.ClientRepository;
import br.com.controle.financeiro.model.repository.InstitutionRepository;
import br.com.controle.financeiro.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
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
@RequestMapping(value = "/api/card", produces = MediaType.APPLICATION_JSON_VALUE)
public class CardController {

    private static final Logger LOG = LoggerFactory.getLogger(CardController.class);

    private final CardRepository cardRepository;
    private final CardDTOResourceAssembler cardDTOResourceAssembler;
    private final ClientRepository clientRepository;
    private final InstitutionRepository institutionRepository;
    private final UserService userService;

    public CardController(final CardRepository cardRepository, final CardDTOResourceAssembler cardDTOResourceAssembler,
                          final ClientRepository clientRepository, final InstitutionRepository institutionRepository,
                          final UserService userService) {
        this.cardRepository = cardRepository;
        this.cardDTOResourceAssembler = cardDTOResourceAssembler;
        this.clientRepository = clientRepository;
        this.institutionRepository = institutionRepository;
        this.userService = userService;
    }

    @GetMapping
    public CollectionModel<EntityModel<CardDTO>> allCards() {
        LOG.debug("finding allCards");
        UserEntity owner = userService.getAuthenticatedUser();
        final List<EntityModel<CardDTO>> cards =
                cardRepository.findAllByOwner(owner).stream().map(CardDTO::fromCard).map(cardDTOResourceAssembler::toModel)
                              .collect(Collectors.toList());

        return new CollectionModel<>(cards, linkTo(methodOn(CardController.class).allCards()).withSelfRel());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
    public EntityModel<CardDTO> newCard(@RequestBody @Valid final CardDTO card) {
        LOG.debug("creating newCard");
        //TODO extract to service
        UserEntity owner = userService.getAuthenticatedUser();
        Client client = clientRepository.findByIdAndOwner(card.getClient(), owner).orElseThrow();
        Institution institution = institutionRepository.findById(card.getInstitution()).orElseThrow();
        CardDTO savedCard = CardDTO.fromCard(cardRepository.save(card.toCard(client, institution, owner)));
        return cardDTOResourceAssembler.toModel(savedCard);
    }

    @GetMapping(path = "/{id}")
    public EntityModel<CardDTO> oneCard(@PathVariable(value = "id") final UUID id) {
        LOG.debug("searching oneCard ${}", id);
        UserEntity owner = userService.getAuthenticatedUser();
        final Card savedCard = cardRepository.findByIdAndOwner(id, owner).orElseThrow(() -> new CardNotFoundException(id));
        return cardDTOResourceAssembler.toModel(CardDTO.fromCard(savedCard));
    }

    @PutMapping(path = "/{id}")
    public EntityModel<CardDTO> replaceCard(@RequestBody final CardDTO newCard, @PathVariable final UUID id) {
        LOG.info("replaceCard");
        //TODO verify DTO integrity
        final UserEntity owner = userService.getAuthenticatedUser();
        Card savedCard = cardRepository.findByIdAndOwner(id, owner).map(card -> {
            card.setName(newCard.getName());
            return cardRepository.save(card);
        }).orElseGet(() -> {
            //TODO extract to service
            Client c = clientRepository.findByIdAndOwner(newCard.getClient(), owner).orElseThrow();
            Institution i = institutionRepository.findById(newCard.getInstitution()).orElseThrow();
            newCard.setId(id);
            return cardRepository.save(newCard.toCard(c, i, owner));
        });

        return cardDTOResourceAssembler.toModel(CardDTO.fromCard(savedCard));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{id}")
    public void deleteCard(@PathVariable final UUID id) {
        LOG.debug("trying to deleteCard ${}", id);
        //TODO verify authenticated permission
        cardRepository.deleteById(id);
    }

}
