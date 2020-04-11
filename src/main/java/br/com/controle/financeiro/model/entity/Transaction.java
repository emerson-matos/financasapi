package br.com.controle.financeiro.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long expenseId;

    private String name;

    private Date transactionDate;
    private BigDecimal value;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client owner;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private BankAccount account;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    public Transaction() {
        super();
    }

    public Transaction(final BigDecimal value, final String name, final Date date, final Client owner,
            final BankAccount account, final Card card) {
        super();
        this.setName(name);
        this.setTransactionDate(date);
        this.setOwner(owner);
        this.setValue(value);
        this.setBankAccount(account);
        this.setCard(card);
    }

    public Transaction(final BigDecimal value, final String name, final Date date, final Client owner,
            final BankAccount account, final Card card, final Long expenseId) {
        super();
        this.setId(expenseId);
        this.setName(name);
        this.setTransactionDate(date);
        this.setOwner(owner);
        this.setValue(value);
        this.setBankAccount(account);
        this.setCard(card);
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
