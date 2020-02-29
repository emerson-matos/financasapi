package br.com.controle.financeiro.controller.linkbuilder;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import br.com.controle.financeiro.controller.ClienteController;
import br.com.controle.financeiro.model.entity.Client;

@Component
public class ClientResourceAssembler implements ResourceAssembler<Client, Resource<Client>> {

	@Override
	public Resource<Client> toResource(Client entity) {
		return new Resource<>(entity,
				linkTo(methodOn(ClienteController.class).oneClient(entity.getId())).withSelfRel(),
				linkTo(methodOn(ClienteController.class).allClients()).withRel("Clients"));
	}

}
