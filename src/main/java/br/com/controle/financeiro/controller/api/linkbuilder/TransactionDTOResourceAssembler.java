package br.com.controle.financeiro.controller.api.linkbuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import br.com.controle.financeiro.controller.api.TransactionController;
import br.com.controle.financeiro.model.dto.TransactionDTO;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class TransactionDTOResourceAssembler implements RepresentationModelAssembler<TransactionDTO, EntityModel<TransactionDTO>> {

    @Override
    public EntityModel<TransactionDTO> toModel(TransactionDTO entity) {
        return new EntityModel<>(entity, linkTo(methodOn(TransactionController.class).oneTransaction(entity.getId()))
                .withSelfRel(),
                              linkTo(methodOn(TransactionController.class).allTransactions()).withRel("transactions"));
    }

}
