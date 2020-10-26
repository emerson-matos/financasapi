package br.com.controle.financeiro.controller.api;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.Valid;

import br.com.controle.financeiro.controller.api.linkbuilder.TransactionDTOResourceAssembler;
import br.com.controle.financeiro.model.dto.TransactionDTO;
import br.com.controle.financeiro.model.entity.BankAccount;
import br.com.controle.financeiro.model.entity.Card;
import br.com.controle.financeiro.model.entity.Client;
import br.com.controle.financeiro.model.entity.Transaction;
import br.com.controle.financeiro.model.entity.UserEntity;
import br.com.controle.financeiro.model.exception.TransactionNotFoundException;
import br.com.controle.financeiro.model.repository.BankAccountRepository;
import br.com.controle.financeiro.model.repository.CardRepository;
import br.com.controle.financeiro.model.repository.ClientRepository;
import br.com.controle.financeiro.model.repository.TransactionRepository;
import br.com.controle.financeiro.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/transaction", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransactionController {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionController.class);

    private final BankAccountRepository accountRepository;
    private final CardRepository cardRepository;
    private final ClientRepository clientRepository;
    private final TransactionRepository transactionRepository;
    private final UserService userService;

    private final TransactionDTOResourceAssembler transactionDTOResourceAssembler;

    public TransactionController(final BankAccountRepository accountRepository, final CardRepository cardRepository,
                                 final ClientRepository clientRepository,
                                 final TransactionRepository transactionRepository, final UserService userService,
                                 final TransactionDTOResourceAssembler transactionDTOResourceAssembler) {
        this.accountRepository = accountRepository;
        this.cardRepository = cardRepository;
        this.clientRepository = clientRepository;
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.transactionDTOResourceAssembler = transactionDTOResourceAssembler;
    }

    @GetMapping
    public CollectionModel<EntityModel<TransactionDTO>> allTransactions() {
        LOG.debug("finding allTransactions");
        final UserEntity owner = userService.getAuthenticatedUser();
        final List<EntityModel<TransactionDTO>> transactions =
                transactionRepository.findAllByOwner(owner).stream().map(TransactionDTO::fromTransaction)
                                     .map(transactionDTOResourceAssembler::toModel).collect(Collectors.toList());

        return new CollectionModel<>(transactions,
                               linkTo(methodOn(TransactionController.class).allTransactions()).withSelfRel());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
    public EntityModel<TransactionDTO> newTransaction(@RequestBody @Valid final TransactionDTO transaction) {
        LOG.debug("creating newTransaction");
        //TODO deal when an transaction doesn't belong to a card and a bank account simultaneously
        //TODO extract to service
        final UserEntity owner = userService.getAuthenticatedUser();
        Client client = clientRepository.findByIdAndOwner(transaction.getClient(), owner).orElseThrow();
        BankAccount account = accountRepository.findByIdAndOwner(transaction.getAccount(), owner).orElseThrow();
        Card card = cardRepository.findByIdAndOwner(transaction.getCard(), owner).orElseThrow();

        TransactionDTO savedTransaction = TransactionDTO
                .fromTransaction(transactionRepository.save(transaction.toTransaction(client, account, card, owner)));

        return transactionDTOResourceAssembler.toModel(savedTransaction);
    }

    @GetMapping(path = "/{id}")
    public EntityModel<TransactionDTO> oneTransaction(@PathVariable(value = "id") final UUID id) {
        LOG.debug("searching oneTransaction ${}", id);
        final UserEntity owner = userService.getAuthenticatedUser();
        final Transaction transaction =
                transactionRepository.findByIdAndOwner(id, owner).orElseThrow(() -> new TransactionNotFoundException(id));

        return transactionDTOResourceAssembler.toModel(TransactionDTO.fromTransaction(transaction));
    }

    @PutMapping(path = "/{id}")
    public EntityModel<TransactionDTO> replaceTransaction(@RequestBody final TransactionDTO newTransaction,
                                                       @PathVariable final UUID id) {
        LOG.info("replaceTransaction");
        //TODO verify DTO integrity
        final UserEntity owner = userService.getAuthenticatedUser();
        Transaction savedTransaction = transactionRepository.findByIdAndOwner(id, owner).map(transaction -> {
            transaction.setName(newTransaction.getName());
            return transactionRepository.save(transaction);
        }).orElseGet(() -> {
            //TODO extract to service
            Client client = clientRepository.findByIdAndOwner(newTransaction.getClient(), owner).orElseThrow();
            BankAccount account = accountRepository.findByIdAndOwner(newTransaction.getAccount(), owner).orElseThrow();
            Card card = cardRepository.findByIdAndOwner(newTransaction.getCard(), owner).orElseThrow();
            newTransaction.setId(id);

            return transactionRepository.save(newTransaction.toTransaction(client, account, card, owner));
        });

        return transactionDTOResourceAssembler.toModel(TransactionDTO.fromTransaction(savedTransaction));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{id}")
    public void deleteTransaction(@PathVariable final UUID id) {
        LOG.debug("trying to deleteTransaction ${}", id);
        //TODO verify authenticated permission
        transactionRepository.deleteById(id);
    }

}
