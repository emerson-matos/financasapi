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

import br.com.controle.financeiro.controller.linkbuilder.ClientDTOResourceAssembler;
import br.com.controle.financeiro.model.dto.ClientDTO;
import br.com.controle.financeiro.model.entity.Client;
import br.com.controle.financeiro.model.exception.ClientNotFoundException;
import br.com.controle.financeiro.model.repository.ClientRepository;

@RestController
@RequestMapping("/client")
public class ClientController {

	private static final Logger log = LoggerFactory.getLogger(ClientController.class);

	private final ClientRepository clientRepository;

	private final ClientDTOResourceAssembler clientDTOResourceAssembler;

	public ClientController(final ClientRepository clientRepository,
			final ClientDTOResourceAssembler clientDTOResourceAssembler) {
		this.clientRepository = clientRepository;
		this.clientDTOResourceAssembler = clientDTOResourceAssembler;
	}

	@GetMapping
	public Resources<Resource<ClientDTO>> allClients() {
		log.debug("finding allClients");

		final List<ClientDTO> clients = clientRepository.findAll().stream().map(ClientDTO::fromClient).collect(Collectors.toList());
		List<Resource<ClientDTO>> cr = clients.stream().map(clientDTOResourceAssembler::toResource).collect(Collectors.toList());

		return new Resources<>(cr, linkTo(methodOn(ClientController.class).allClients()).withSelfRel());
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public Resource<ClientDTO> newClient(@RequestBody final ClientDTO client) {
		log.debug("creating newClient");
		ClientDTO clientDTO = ClientDTO.fromClient(clientRepository.save(client.toClient()));
		return clientDTOResourceAssembler.toResource(clientDTO);
	}

	@GetMapping(path = "/{id}")
	public Resource<ClientDTO> oneClient(@PathVariable(value = "id") final long id) {
		log.debug("searching oneClient {}", id);
		Client client = clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException(id));
		return clientDTOResourceAssembler.toResource(ClientDTO.fromClient(client));
	}

	@PutMapping(path = "/{id}")
	public ClientDTO replaceClient(@RequestBody final ClientDTO newClient, @PathVariable final Long id) {
		log.info("replaceClient");
		Client savedClient = clientRepository.findById(id).map(client -> {
			client.setName(newClient.getName());
			return clientRepository.save(client);
		}).orElseGet(() -> {
			newClient.setId(id);
			return clientRepository.save(newClient.toClient());
		});
		return ClientDTO.fromClient(savedClient);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(path = "/{id}")
	public void deleteClient(@PathVariable final Long id) {
		log.debug("trying to deleteClient {}", id);
		clientRepository.deleteById(id);
	}
}
