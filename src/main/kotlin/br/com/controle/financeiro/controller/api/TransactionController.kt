package br.com.controle.financeiro.controller.api

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.http.MediaType
import org.springframework.http.HttpStatus
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.slf4j.LoggerFactory
import br.com.controle.financeiro.model.repository.BankAccountRepository
import br.com.controle.financeiro.model.repository.CardRepository
import br.com.controle.financeiro.model.repository.ClientRepository
import br.com.controle.financeiro.model.repository.TransactionRepository
import br.com.controle.financeiro.model.repository.UserRepository
import br.com.controle.financeiro.model.dto.TransactionDTO
import br.com.controle.financeiro.model.entity.Transaction
import br.com.controle.financeiro.model.exception.TransactionNotFoundException
import br.com.controle.financeiro.controller.api.linkbuilder.TransactionDTOResourceAssembler
import jakarta.validation.Valid
import java.util.UUID

@RequestMapping(value = ["/api/transaction"], produces = [MediaType.APPLICATION_JSON_VALUE])
class TransactionController(
    private val accountRepository: BankAccountRepository, private val cardRepository: CardRepository,
    private val clientRepository: ClientRepository,
    private val transactionRepository: TransactionRepository, private val userRepository: UserRepository,
    private val transactionDTOResourceAssembler: TransactionDTOResourceAssembler
) {
    @GetMapping
    fun allTransactions(): CollectionModel<EntityModel<TransactionDTO>> {
        LOG.debug("finding allTransactions")
        val owner = userRepository.findAll().iterator().next()
        val transactions = transactionRepository.findAllByOwner(owner).stream().map { t: Transaction ->
            TransactionDTO.fromTransaction(
                t
            )
        }
            .map { entity: TransactionDTO -> transactionDTOResourceAssembler.toModel(entity) }
        .toList()
        return CollectionModel.of(
            transactions,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TransactionController::class.java).allTransactions())
                .withSelfRel()
        )
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun newTransaction(@RequestBody transaction: @Valid TransactionDTO): EntityModel<TransactionDTO> {
        LOG.debug("creating newTransaction")
        //TODO deal when an transaction doesn't belong to a card and a bank account simultaneously
        //TODO extract to service
        val owner = userRepository.findAll().iterator().next()
        val client = clientRepository.findByIdAndOwner(transaction.client, owner).orElseThrow()
        val account = accountRepository.findByIdAndOwner(transaction.account, owner).orElseThrow()
        val card = cardRepository.findByIdAndOwner(transaction.card, owner).orElseThrow()
        // val savedTransaction: TransactionDTO = TransactionDTO.fromTransaction(
            // transactionRepository.save(
               // transaction.toTransaction(client, account, card, owner)
            // )
        // )
        return transactionDTOResourceAssembler.toModel(transaction)
    }

    @GetMapping(path = ["/{id}"])
    fun oneTransaction(@PathVariable(value = "id") id: UUID): EntityModel<TransactionDTO> {
        LOG.debug("searching oneTransaction \${}", id)
        val owner = userRepository.findAll().iterator().next()
        val transaction =
            transactionRepository.findByIdAndOwner(id, owner).orElseThrow { TransactionNotFoundException(id) }
        return transactionDTOResourceAssembler.toModel(TransactionDTO.fromTransaction(transaction))
    }

    // @PutMapping(path = ["/{id}"])
    // fun replaceTransaction(
    //     @RequestBody newTransaction: TransactionDTO,
    //     @PathVariable id: UUID
    // ): EntityModel<TransactionDTO> {
    //     LOG.info("replaceTransaction")
    //     //TODO verify DTO integrity
    //     val owner = userRepository.findAll().iterator().next()
    //     val savedTransaction = transactionRepository.findByIdAndOwner(id, owner).map { transaction: Transaction ->
    //         // transaction.name = newTransaction.name
    //         transactionRepository.save(transaction)
    //     }.orElseGet {
    //         //TODO extract to service
    //         val client = clientRepository.findByIdAndOwner(newTransaction.client, owner).orElseThrow()
    //         val account = accountRepository.findByIdAndOwner(newTransaction.account, owner).orElseThrow()
    //         val card = cardRepository.findByIdAndOwner(newTransaction.card, owner).orElseThrow()
    //         newTransaction.id = id
    //         transactionRepository.save(newTransaction.toTransaction(client, account, card, owner))
    //     }
    //     return transactionDTOResourceAssembler.toModel(TransactionDTO.fromTransaction(savedTransaction))
    // }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = ["/{id}"])
    fun deleteTransaction(@PathVariable id: UUID) {
        LOG.debug("trying to deleteTransaction \${}", id)
        //TODO verify authenticated permission
        // transactionRepository.deleteById(id)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(TransactionController::class.java)
    }
}
