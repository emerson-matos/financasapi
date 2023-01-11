package br.com.controle.financeiro.controller.api

import br.com.controle.financeiro.controller.api.linkbuilder.CardDTOResourceAssembler
import br.com.controle.financeiro.model.dto.CardDTO
import br.com.controle.financeiro.model.entity.Card
import br.com.controle.financeiro.model.repository.CardRepository
import br.com.controle.financeiro.model.repository.ClientRepository
import br.com.controle.financeiro.model.repository.InstitutionRepository
import br.com.controle.financeiro.model.repository.UserRepository
import br.com.controle.financeiro.model.exception.CardNotFoundException
import jakarta.validation.Valid
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.http.MediaType
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.slf4j.LoggerFactory
import java.util.UUID

@RestController
@RequestMapping(value = ["/api/card"], produces = [MediaType.APPLICATION_JSON_VALUE])
class CardController(
        private val cardRepository: CardRepository,
        private val cardDTOResourceAssembler: CardDTOResourceAssembler,
        private val clientRepository: ClientRepository,
        private val institutionRepository: InstitutionRepository,
        private val userRepository: UserRepository
) {
    @GetMapping
    fun allCards(): CollectionModel<EntityModel<CardDTO>> {
        LOG.debug("finding allCards")
        val owner = userRepository.findAll().iterator().next()
        val cards =
                cardRepository
                        .findAllByOwner(owner)
                        .stream()
                        .map { card: Card -> CardDTO.fromCard(card) }
                        .map { entity: CardDTO -> cardDTOResourceAssembler.toModel(entity) }
                        .toList()
        return CollectionModel.of(
                cards,
                WebMvcLinkBuilder.linkTo(
                                WebMvcLinkBuilder.methodOn(CardController::class.java).allCards()
                        )
                        .withSelfRel()
        )
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun newCard(@RequestBody card: @Valid CardDTO): EntityModel<CardDTO> {
        LOG.debug("creating newCard")
        // TODO extract to service
        val owner = userRepository.findAll().iterator().next()
        val client = clientRepository.findByIdAndOwner(card.client.id, owner).orElseThrow()
        val institution =
                card.institution.id.let { institutionRepository.findById(it).orElseThrow() }
        val savedCard: CardDTO =
                CardDTO.fromCard(cardRepository.save(card.toCard(client, institution, owner)))
        return cardDTOResourceAssembler.toModel(savedCard)
    }

    @GetMapping(path = ["/{id}"])
    fun oneCard(@PathVariable(value = "id") id: UUID): EntityModel<CardDTO> {
        LOG.debug("searching oneCard \${}", id)
        val owner = userRepository.findAll().iterator().next()
        val savedCard =
                cardRepository.findByIdAndOwner(id, owner).orElseThrow { CardNotFoundException(id) }
        return cardDTOResourceAssembler.toModel(CardDTO.fromCard(savedCard))
    }

    // @PutMapping(path = ["/{id}"])
    // fun replaceCard(@RequestBody newCard: CardDTO, @PathVariable id: UUID): EntityModel<CardDTO> {
        // LOG.info("replaceCard")
        // TODO verify DTO integrity
        // val owner = userRepository.findAll().iterator().next()
        // val savedCard =
                // cardRepository
                        // .findByIdAndOwner(id, owner)
                        // .map { card: Card ->
                            // card.name = newCard.name
                            // cardRepository.save(card)
                        // }
                        // .orElseGet {
                            //
                            // TODO extract to service
                            // val c =
                                    // clientRepository
                                            // .findByIdAndOwner(newCard.client, owner)
                                            // .orElseThrow()
                            // val i =
                                    // institutionRepository
                                            // .findById(newCard.institution)
                                            // .orElseThrow()!!
                            // newCard.id = id
                            // cardRepository.save(newCard.toCard(c, i, owner))
                        // }
        // return cardDTOResourceAssembler.toModel(CardDTO.fromCard(savedCard))
    // }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = ["/{id}"])
    fun deleteCard(@PathVariable id: UUID) {
        LOG.debug("trying to deleteCard \${}", id)
        // TODO verify authenticated permission
        cardRepository.deleteById(id)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(CardController::class.java)
    }
}
