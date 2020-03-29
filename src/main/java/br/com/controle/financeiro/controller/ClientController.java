package br.com.controle.financeiro.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.controle.financeiro.controller.linkbuilder.ClientResourceAssembler;
import br.com.controle.financeiro.model.entity.Client;
import br.com.controle.financeiro.model.exception.ClientNotFoundException;
import br.com.controle.financeiro.model.repository.ClientRepository;

@RestController
@RequestMapping("/client")
public class ClientController {

	private static final Logger log = LoggerFactory.getLogger(ClientController.class);

	private final ClientRepository clientRepository;

	private final ClientResourceAssembler clientResourceAssembler;

	public ClientController(final ClientRepository clientRepository,
			final ClientResourceAssembler clientResourceAssembler) {
		this.clientRepository = clientRepository;
		this.clientResourceAssembler = clientResourceAssembler;
	}

	@RequestMapping(path = "", method = RequestMethod.GET)
	public Resources<Resource<Client>> allClients() {
		log.debug("finding allClients");

		final List<Resource<Client>> clients = clientRepository.findAll().stream()
				.map(clientResourceAssembler::toResource).collect(Collectors.toList());

		return new Resources<>(clients, linkTo(methodOn(ClientController.class).allClients()).withSelfRel());
	}

	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(path = "", method = RequestMethod.POST)
	public Resource<Client> newClient(@RequestBody final Client client) throws URISyntaxException {
		log.debug("creating newClient");
		return clientResourceAssembler.toResource(clientRepository.save(client));
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public Resource<Client> oneClient(@PathVariable(value = "id") final long id) {
		log.debug("searching oneClient " + id);
		final Client c = clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException(id));
		return clientResourceAssembler.toResource(c);
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public Client replaceClient(@RequestBody final Client newClient, @PathVariable final Long id) {
		System.out.println(getClass() + " replaceClient");
		return clientRepository.findById(id).map(client -> {
			client.setName(newClient.getName());
			return clientRepository.save(client);
		}).orElseGet(() -> {
			newClient.setId(id);
			return clientRepository.save(newClient);
		});
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public void deleteClient(@PathVariable final Long id) {
		log.debug("trying to deleteClient " + id);
		clientRepository.deleteById(id);
	}
}
