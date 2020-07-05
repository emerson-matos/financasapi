package br.com.controle.financeiro.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.com.controle.financeiro.model.entity.BankAccount;
import br.com.controle.financeiro.model.entity.Card;
import br.com.controle.financeiro.model.entity.Client;
import br.com.controle.financeiro.model.entity.Transaction;

public class TransactionDTO implements Serializable {

	private Long expenseId;
	@NotNull
	private String name;
	@NotNull
	private Date transactionDate;
	@NotNull
	private BigDecimal value;
	@NotNull
	private Currency currency;

	@JsonProperty(access = Access.WRITE_ONLY)
	private Long ownerId;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private Long accountId;

	@JsonProperty(access = Access.WRITE_ONLY)
	private Long cardId;

	@JsonProperty(access = Access.READ_ONLY)
	private Client owner;

	@JsonProperty(access = Access.READ_ONLY)
	private BankAccount account;
	
	@JsonProperty(access = Access.READ_ONLY)
	private Card card;

	public TransactionDTO() {
		super();
	}

	public TransactionDTO(final BigDecimal value, final Currency currency, final String name, final Date date, final Long owner,
			final Long account, final Long card, final Long id) {
		super();
		this.setCurrency(currency);
		this.setId(id);
		this.setName(name);
		this.setTransactionDate(date);
		this.setOwnerId(owner);
		this.setValue(value);
		this.setBankAccountId(account);
		this.setCardId(card);
	}

	public static TransactionDTO fromTransaction(Transaction t) {
		return new TransactionDTO(t.getValue(), t.getCurrency(), t.getName(), t.getTransactionDate(), t.getOwner().getId(),
				t.getAccount().getId(), t.getCard().getId(), t.getId());
	}

	public Transaction toTransaction() {
		Card cardObj = new Card().withId(this.cardId);
		BankAccount accountObj = new BankAccount().withId(this.accountId);
		Client client = new Client().withId(this.ownerId);

		return new Transaction(this.getValue(), this.getCurrency(), this.getName(), this.getTransactionDate(), client, accountObj, cardObj,
				this.getId());
	}

	public void setCard(final Card card) {
		this.card = card;
	}

	public Card getCard() {
		return this.card;
	}

	public void setCardId(final Long id) {
		this.cardId = id;
	}

	public Long getCardId() {
		return this.cardId;
	}

	public void setBankAccountId(final Long accountId) {
		this.accountId = accountId;
	}

	public Long getBankAccountId() {
		return this.accountId;
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

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(final Long owner) {
		this.ownerId = owner;
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

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
}