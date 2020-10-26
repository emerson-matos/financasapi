package br.com.controle.financeiro.controller.api.linkbuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import br.com.controle.financeiro.controller.api.ClientController;
import br.com.controle.financeiro.model.dto.ClientDTO;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ClientDTOResourceAssembler implements RepresentationModelAssembler<ClientDTO, EntityModel<ClientDTO>> {

    @Override
    public EntityModel<ClientDTO> toModel(ClientDTO entity) {
        return new EntityModel<>(entity, linkTo(methodOn(ClientController.class).oneClient(entity.getId())).withSelfRel(),
                              linkTo(methodOn(ClientController.class).allClients()).withRel("clients"));
    }

}
