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

import br.com.controle.financeiro.controller.linkbuilder.ClientResourceAssembler;
import br.com.controle.financeiro.model.entity.Client;
import br.com.controle.financeiro.model.exception.ClientNotFoundException;
import br.com.controle.financeiro.model.repository.ClientRepository;

@RestController
@RequestMapping("/client")
public class ClientController {

	private static final Logger LOG = LoggerFactory.getLogger(ClientController.class);

	private final ClientRepository clientRepository;

	private final ClientResourceAssembler clientResourceAssembler;

	public ClientController(final ClientRepository clientRepository,
			final ClientResourceAssembler clientResourceAssembler) {
		this.clientRepository = clientRepository;
		this.clientResourceAssembler = clientResourceAssembler;
	}

	@GetMapping
	public Resources<Resource<Client>> allClients() {
		LOG.debug("finding allClients");

		final List<Resource<Client>> clients = clientRepository.findAll().stream()
				.map(clientResourceAssembler::toResource).collect(Collectors.toList());

		return new Resources<>(clients, linkTo(methodOn(ClientController.class).allClients()).withSelfRel());
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public Resource<Client> newClient(@RequestBody final Client client) {
		LOG.debug("creating newClient");
		return clientResourceAssembler.toResource(clientRepository.save(client));
	}

	@GetMapping(path = "/{id}")
	public Resource<Client> oneClient(@PathVariable(value = "id") final long id) {
		LOG.debug("searching oneClient ${}", id);
		final Client c = clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException(id));
		return clientResourceAssembler.toResource(c);
	}

	@PutMapping(path = "/{id}")
	public Client replaceClient(@RequestBody final Client newClient, @PathVariable final Long id) {
		LOG.info("replaceClient");
		return clientRepository.findById(id).map(client -> {
			client.setName(newClient.getName());
			return clientRepository.save(client);
		}).orElseGet(() -> {
			newClient.setId(id);
			return clientRepository.save(newClient);
		});
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(path = "/{id}")
	public void deleteClient(@PathVariable final Long id) {
		LOG.debug("trying to deleteClient ${}", id);
		clientRepository.deleteById(id);
	}
}
