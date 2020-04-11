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
	private Long owner;
	private Long account;
	private Long card;

	public TransactionDTO() {
		super();
	}

	public TransactionDTO(final BigDecimal value, final String name, final Date date, final Long owner,
			final Long account, final Long card, final Long id) {
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
		return new TransactionDTO(t.getValue(), t.getName(), t.getTransactionDate(), t.getOwner().getId(),
				t.getAccount().getId(), t.getCard().getId(), t.getId());
	}

	public Transaction toTransaction() {
		Card cardObj = new Card().withId(this.card);
		BankAccount accountObj = new BankAccount().withId(this.account);
		Client client = new Client().withId(owner);

		return new Transaction(this.getValue(), this.getName(), this.getTransactionDate(), client, accountObj, cardObj,
				this.getId());
	}

	public void setCard(final Long card) {
		this.card = card;
	}

	public Long getCard() {
		return this.card;
	}

	public void setBankAccount(final Long account) {
		this.account = account;
	}

	public Long getBankAccount() {
		return this.account;
	}

	public BigDecimal getValue() {
		return value;
	}

	public Long getOwner() {
		return owner;
	}

	public void setOwner(final Long owner) {
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

}