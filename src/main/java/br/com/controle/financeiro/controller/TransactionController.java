package br.com.controle.financeiro.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.controle.financeiro.controller.linkbuilder.TransactionResourceAssembler;
import br.com.controle.financeiro.model.entity.Transaction;
import br.com.controle.financeiro.model.exception.TransactionNotFoundException;
import br.com.controle.financeiro.model.repository.TransactionRepository;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

	private static final Logger LOG = LoggerFactory.getLogger(TransactionController.class);

	private final TransactionRepository transactionRepository;

	private final TransactionResourceAssembler transactionResourceAssembler;

	public TransactionController(final TransactionRepository transactionRepository,
			final TransactionResourceAssembler transactionResourceAssembler) {
		this.transactionRepository = transactionRepository;
		this.transactionResourceAssembler = transactionResourceAssembler;
	}

	@GetMapping
	public Resources<Resource<Transaction>> allTransactions() {
		LOG.debug("finding allTransactions");

		final List<Resource<Transaction>> transactions = transactionRepository.findAll().stream()
				.map(transactionResourceAssembler::toResource).collect(Collectors.toList());

		return new Resources<>(transactions, linkTo(methodOn(TransactionController.class).allTransactions()).withSelfRel());
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public Resource<Transaction> newTransaction(@RequestBody final Transaction transaction) {
		LOG.debug("creating newTransaction");
		return transactionResourceAssembler.toResource(transactionRepository.save(transaction));
	}

	@GetMapping(path = "/{id}")
	public Resource<Transaction> oneTransaction(@PathVariable(value = "id") final long id) {
		LOG.debug("searching oneTransaction ${}", id);
		final Transaction c = transactionRepository.findById(id).orElseThrow(() -> new TransactionNotFoundException(id));
		return transactionResourceAssembler.toResource(c);
	}

	@PutMapping(path = "/{id}")
	public Transaction replaceTransaction(@RequestBody final Transaction newTransaction, @PathVariable final Long id) {
		LOG.info("replaceTransaction");
		return transactionRepository.findById(id).map(transaction -> {
			transaction.setName(newTransaction.getName());
			return transactionRepository.save(transaction);
		}).orElseGet(() -> {
			newTransaction.setId(id);
			return transactionRepository.save(newTransaction);
		});
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping(path = "/{id}")
	public void deleteTransaction(@PathVariable final Long id) {
		LOG.debug("trying to deleteTransaction ${}", id);
		transactionRepository.deleteById(id);
	}
}
