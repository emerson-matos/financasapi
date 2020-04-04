package br.com.controle.financeiro.model.entity;

import java.io.Serializable;
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
    private double value;

    @ManyToOne
    @JoinColumn(name = "ownerId")
    private Client owner;

    @ManyToOne
    @JoinColumn(name = "accountId")
    private BankAccount account;

    @ManyToOne
    @JoinColumn(name = "cardId")
    private Card card;

    public Transaction() {
        super();
    }

    public Transaction(final double value, final String name, final Date date, final Client owner,
            final BankAccount account, final Card card) {
        super();
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

    public double getValue() {
        return value;
    }

    public Client getOwner() {
        return owner;
    }

    public void setOwner(final Client owner) {
        this.owner = owner;
    }

    public void setValue(final double value) {
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

}