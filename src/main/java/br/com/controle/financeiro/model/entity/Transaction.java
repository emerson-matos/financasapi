package br.com.controle.financeiro.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "transaction")
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_expense")
    private UUID id;

    private String name;

    private LocalDateTime transactionDate;
    private BigDecimal value;
    private Currency currency;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_client")
    private Client owner;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_account")
    private BankAccount account;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_card")
    private Card card;

    public Transaction() {
        super();
    }

    public Transaction(UUID id, String name, LocalDateTime transactionDate, BigDecimal value, Currency currency,
                       Client owner, BankAccount account, Card card) {
        super();
        this.id = id;
        this.name = name;
        this.transactionDate = transactionDate;
        this.value = value;
        this.currency = currency;
        this.owner = owner;
        this.account = account;
        this.card = card;
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

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(final LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID expenseId) {
        this.id = expenseId;
    }

    public BankAccount getAccount() {
        return account;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

}
