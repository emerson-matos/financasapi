package br.com.controle.financeiro.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.controle.financeiro.model.entity.BankAccount;
import br.com.controle.financeiro.model.entity.Card;
import br.com.controle.financeiro.model.entity.Client;
import br.com.controle.financeiro.model.entity.Transaction;

public class TransactionDTO implements Serializable {

	private Long expenseId;
	private String name;
	private Date transactionDate;
	private BigDecimal value;
	private Client owner;
	private BankAccount account;
	private Card card;

	public TransactionDTO() {
		super();
	}

	public TransactionDTO(final BigDecimal value, final String name, final Date date, final Client owner,
			final BankAccount account, final Card card, final Long id) {
		super();
		this.setId(id);
		this.setName(name);
		this.setTransactionDate(date);
		this.setOwner(owner);
		this.setValue(value);
		this.setBankAccount(account);
		this.setCard(card);
	}

	public static TransactionDTO fromTransaction(Transaction t) {
		return new TransactionDTO(t.getValue(), t.getName(), t.getTransactionDate(), t.getOwner(), t.getAccount(),
				t.getCard(), t.getId());
	}

	public Transaction toTransaction() {
		return new Transaction(this.getValue(), this.getName(), this.getTransactionDate(), this.getOwner(), this.getAccount(),
		this.getCard(), this.getId());
	}

	public void setCard(final Card card) {
		this.card = card;
	}

	public Card getCard() {
		return this.card;
	}

	public void setBankAccount(final BankAccount account) {
		this.account = account;
	}

	public BankAccount getBankAccount() {
		return this.account;
	}

	public BigDecimal getValue() {
		return value;
	}

	public Client getOwner() {
		return owner;
	}

	public void setOwner(final Client owner) {
		this.owner = owner;
	}

	public void setValue(final BigDecimal value) {
		this.value = value;
	}

	public Date getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(final Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Long getId() {
		return expenseId;
	}

	public void setId(Long expenseId) {
		this.expenseId = expenseId;
	}

	public BankAccount getAccount() {
		return account;
	}

}