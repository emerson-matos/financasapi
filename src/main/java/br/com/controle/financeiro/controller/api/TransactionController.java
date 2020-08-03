package br.com.controle.financeiro.controller.api;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import br.com.controle.financeiro.controller.api.linkbuilder.TransactionDTOResourceAssembler;
import br.com.controle.financeiro.model.dto.TransactionDTO;
import br.com.controle.financeiro.model.entity.BankAccount;
import br.com.controle.financeiro.model.entity.Card;
import br.com.controle.financeiro.model.entity.Client;
import br.com.controle.financeiro.model.entity.Transaction;
import br.com.controle.financeiro.model.exception.TransactionNotFoundException;
import br.com.controle.financeiro.model.repository.BankAccountRepository;
import br.com.controle.financeiro.model.repository.CardRepository;
import br.com.controle.financeiro.model.repository.ClientRepository;
import br.com.controle.financeiro.model.repository.TransactionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
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
@RequestMapping(value = "/api/transaction", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class TransactionController {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionController.class);

    private final BankAccountRepository accountRepository;
    private final CardRepository cardRepository;
    private final ClientRepository clientRepository;
    private final TransactionRepository transactionRepository;

    private final TransactionDTOResourceAssembler transactionDTOResourceAssembler;

    public TransactionController(final BankAccountRepository accountRepository, final CardRepository cardRepository,
                                 final ClientRepository clientRepository, final TransactionRepository transactionRepository,
                                 final TransactionDTOResourceAssembler transactionDTOResourceAssembler) {
        this.accountRepository = accountRepository;
        this.cardRepository = cardRepository;
        this.clientRepository = clientRepository;
        this.transactionRepository = transactionRepository;
        this.transactionDTOResourceAssembler = transactionDTOResourceAssembler;
    }

    @GetMapping
    public Resources<Resource<TransactionDTO>> allTransactions() {
        LOG.debug("finding allTransactions");

        final List<Resource<TransactionDTO>> transactions =
                transactionRepository.findAll().stream().map(TransactionDTO::fromTransaction)
                                     .map(transactionDTOResourceAssembler::toResource).collect(Collectors.toList());

        return new Resources<>(transactions,
                               linkTo(methodOn(TransactionController.class).allTransactions()).withSelfRel());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    public Resource<TransactionDTO> newTransaction(@RequestBody @Valid final TransactionDTO transaction) {
        LOG.debug("creating newTransaction");
        //TODO deal when an transaction doesn't belong to a card and a bank account simultaneously
        //TODO extract to service
        Client owner = clientRepository.findById(transaction.getOwner()).get();
        BankAccount account = accountRepository.findById(transaction.getAccount()).orElse(null);
        Card card = cardRepository.findById(transaction.getCard()).orElse(null);

        TransactionDTO savedTransacation =
                TransactionDTO.fromTransaction(transactionRepository.save(transaction.toTransaction(owner, account,
                                                                                                    card)));
        return transactionDTOResourceAssembler.toResource(savedTransacation);
    }

    @GetMapping(path = "/{id}")
    public Resource<TransactionDTO> oneTransaction(@PathVariable(value = "id") final long id) {
        LOG.debug("searching oneTransaction ${}", id);
        final Transaction transaction =
                transactionRepository.findById(id).orElseThrow(() -> new TransactionNotFoundException(id));
        return transactionDTOResourceAssembler.toResource(TransactionDTO.fromTransaction(transaction));
    }

    @PutMapping(path = "/{id}")
    public Resource<TransactionDTO> replaceTransaction(@RequestBody final TransactionDTO newTransaction,
                                                       @PathVariable final Long id) {
        LOG.info("replaceTransaction");
        //TODO verify DTO integrity
        Transaction savedTransaction = transactionRepository.findById(id).map(transaction -> {
            transaction.setName(newTransaction.getName());
            return transactionRepository.save(transaction);
        }).orElseGet(() -> {
            //TODO extract to service
            Client owner = clientRepository.findById(newTransaction.getOwner()).get();
            BankAccount account = accountRepository.findById(newTransaction.getAccount()).orElse(null);
            Card card = cardRepository.findById(newTransaction.getCard()).orElse(null);
            newTransaction.setId(id);

            return transactionRepository.save(newTransaction.toTransaction(owner, account, card));
        });

        return transactionDTOResourceAssembler.toResource(TransactionDTO.fromTransaction(savedTransaction));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{id}")
    public void deleteTransaction(@PathVariable final Long id) {
        LOG.debug("trying to deleteTransaction ${}", id);
        transactionRepository.deleteById(id);
    }

}
