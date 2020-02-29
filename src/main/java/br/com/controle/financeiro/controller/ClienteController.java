package br.com.controle.financeiro.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.controle.financeiro.controller.linkbuilder.ClientResourceAssembler;
import br.com.controle.financeiro.model.entity.Client;
import br.com.controle.financeiro.model.exception.ClientNotFoundException;
import br.com.controle.financeiro.model.repository.ClientRepository;

@RestController
@RequestMapping("client")
public class ClientController {

	private final ClientRepository clientRepository;

	private final ClientResourceAssembler clientResourceAssembler;

	public ClienteController(ClientRepository clientRepository, ClientResourceAssembler clientResourceAssembler) {
		this.clientRepository = clientRepository;
		this.clientResourceAssembler = clientResourceAssembler;
	}

	@GetMapping({ "/", "" })
	public Resources<Resource<Client>> allClients() {
		System.out.println(getClass() + " allClients");

		List<Resource<Client>> clients = clientRepository.findAll().stream().map(clientResourceAssembler::toResource)
				.collect(Collectors.toList());

		return new Resources<>(clientes, linkTo(methodOn(ClientController.class).allClients()).withSelfRel());
	}

	@PostMapping({ "/", "" })
	public Client newClient(@RequestBody Client client) {
		System.out.println(getClass() + " newClient");
		try {
			return clientRepository.save(client);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@GetMapping({ "{id}", "{id}/" })
	public Resource<Client> oneClient(@PathVariable(value = "id") long id) {
		System.out.println(getClass() + " oneClient");
		Client c = clientRepository.findById(id).orElseThrow(() -> new ClientNotFoundException(id));
		return clientResourceAssembler.toResource(c);
	}

	@PutMapping({ "{id}", "{id}/" })
	public Client replaceClient(@RequestBody Client newClient, @PathVariable Long id) {
		System.out.println(getClass() + " replaceClient");
		return clientRepository.findById(id).map(client -> {
			client.setNome(newClient.getNome());
			return clientRepository.save(client);
		}).orElseGet(() -> {
			newClient.setId(id);
			return clientRepository.save(newClient);
		});
	}

	@DeleteMapping({ "{id}", "{id}/" })
	public void deleteClient(@PathVariable Long id) {
		System.out.println(getClass() + " deleteClient");
		clientRepository.deleteById(id);
	}

}
