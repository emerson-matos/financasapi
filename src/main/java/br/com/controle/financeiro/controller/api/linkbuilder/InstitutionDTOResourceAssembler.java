package br.com.controle.financeiro.controller.api.linkbuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import br.com.controle.financeiro.controller.api.InstitutionController;
import br.com.controle.financeiro.model.dto.InstitutionDTO;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class InstitutionDTOResourceAssembler implements RepresentationModelAssembler<InstitutionDTO, EntityModel<InstitutionDTO>> {

    @Override
    public EntityModel<InstitutionDTO> toModel(InstitutionDTO entity) {
        return new EntityModel<>(entity, linkTo(methodOn(InstitutionController.class).oneInstitution(entity.getId()))
                .withSelfRel(),
                              linkTo(methodOn(InstitutionController.class).allInstitutions()).withRel("institutions"));
    }

}
