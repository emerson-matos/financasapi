package br.com.controle.financeiro.controller.api

import br.com.controle.financeiro.controller.api.linkbuilder.ClientDTOResourceAssembler
import br.com.controle.financeiro.model.dto.ClientDTO
import br.com.controle.financeiro.model.entity.Client
import br.com.controle.financeiro.model.exception.ClientNotFoundException
import br.com.controle.financeiro.model.repository.ClientRepository
import br.com.controle.financeiro.model.repository.UserRepository
import jakarta.validation.Valid
import java.util.UUID
import org.slf4j.LoggerFactory
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/client"], produces = [MediaType.APPLICATION_JSON_VALUE])
class ClientController(
        private val clientRepository: ClientRepository,
        private val userRepository: UserRepository,
        private val clientDTOResourceAssembler: ClientDTOResourceAssembler
) {
    @GetMapping
    fun allClients(): CollectionModel<EntityModel<ClientDTO>> {
        LOG.debug("finding allClients")
        val owner = userRepository.findAll().iterator().next()
        val clients =
                clientRepository
                        .findAllByOwner(owner)
                        .stream()
                        .map { client: Client -> ClientDTO.fromClient(client) }
                        .toList()
        val cr =
                clients.stream()
                        .map { entity: ClientDTO -> clientDTOResourceAssembler.toModel(entity) }
                        .toList()
        return CollectionModel.of(
                cr,
                WebMvcLinkBuilder.linkTo(
                                WebMvcLinkBuilder.methodOn(ClientController::class.java)
                                        .allClients()
                        )
                        .withSelfRel()
        )
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun newClient(@RequestBody client: @Valid ClientDTO): EntityModel<ClientDTO> {
        LOG.debug("creating newClient")
        // TODO extract to service
        val owner = userRepository.findAll().iterator().next()
        val clientDTO: ClientDTO =
                ClientDTO.fromClient(
                        clientRepository.save(
                                ClientDTO(client.name, owner,client.id!!).toClient(owner)
                        )
                )
        return clientDTOResourceAssembler.toModel(clientDTO)
    }

    @GetMapping(path = ["/{id}"])
    fun oneClient(@PathVariable(value = "id") id: UUID): EntityModel<ClientDTO> {
        LOG.debug("searching oneClient {}", id)
        val owner = userRepository.findAll().iterator().next()
        val client =
                clientRepository.findByIdAndOwner(id, owner).orElseThrow {
                    ClientNotFoundException(id)
                }
        return clientDTOResourceAssembler.toModel(ClientDTO.fromClient(client))
    }

    // @PutMapping(path = ["/{id}"])
    // fun replaceClient(@RequestBody newClient: ClientDTO, @PathVariable id: UUID):
    // EntityModel<ClientDTO> {
    // LOG.info("replaceClient")
    // TODO verify authenticated permission
    // TODO verify DTO integrity
    // val savedClient = clientRepository.findById(id).map { client: Client? ->
    // client?.name = newClient.name
    // client?.let { clientRepository.save(it) }
    // }.orElseGet {
    // newClient.id = id
    // TODO extract to service
    // val owner = userRepository.findAll().iterator().next()
    // clientRepository.save(newClient.toClient(owner))
    // }
    // return clientDTOResourceAssembler.toModel(ClientDTO.fromClient(savedClient))
    // }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = ["/{id}"])
    fun deleteClient(@PathVariable id: UUID) {
        LOG.debug("trying to deleteClient {}", id)
        // TODO verify authenticated permission
        clientRepository.deleteById(id)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(ClientController::class.java)
    }
}
