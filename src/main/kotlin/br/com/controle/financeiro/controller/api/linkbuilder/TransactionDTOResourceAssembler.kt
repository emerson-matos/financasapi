package br.com.controle.financeiro.controller.api.linkbuilder

import org.springframework.stereotype.Component
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.hateoas.EntityModel
import br.com.controle.financeiro.model.dto.TransactionDTO
import br.com.controle.financeiro.controller.api.TransactionController

@Component
class TransactionDTOResourceAssembler : RepresentationModelAssembler<TransactionDTO, EntityModel<TransactionDTO>> {
    override fun toModel(entity: TransactionDTO): EntityModel<TransactionDTO> {
        return EntityModel.of(
            entity,
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(
                    TransactionController::class.javaObjectType
                ).oneTransaction(entity.id!!)
            ).withSelfRel(),
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(
                    TransactionController::class.java
                ).allTransactions()
            ).withRel("transactions")
        )
    }
}
