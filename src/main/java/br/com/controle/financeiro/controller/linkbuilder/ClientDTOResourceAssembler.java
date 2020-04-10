package br.com.controle.financeiro.controller.linkbuilder;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import br.com.controle.financeiro.controller.ClientController;
import br.com.controle.financeiro.model.dto.ClientDTO;

@Component
public class ClientDTOResourceAssembler implements ResourceAssembler<ClientDTO, Resource<ClientDTO>> {

	@Override
	public Resource<ClientDTO> toResource(ClientDTO entity) {
		return new Resource<>(entity, linkTo(methodOn(ClientController.class).oneClient(entity.getId())).withSelfRel(),
				linkTo(methodOn(ClientController.class).allClients()).withRel("clients"));
	}

}
