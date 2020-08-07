package br.com.controle.financeiro.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity(name = "transaction")
public class Transaction extends AbstractPersistable<UUID> implements Serializable {

    private String name;

    private LocalDateTime transactionDate;
    private BigDecimal value;
    private Currency currency;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_client")
    private Client responsible;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_account")
    private BankAccount account;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_card")
    private Card card;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "id_user")
    private UserEntity owner;

    public Transaction() {
        super();
    }

    public Transaction(UUID id, String name, LocalDateTime transactionDate, BigDecimal value, Currency currency,
                       Client responsible, BankAccount account, Card card, UserEntity owner) {
        super();
        this.setId(id);
        this.name = name;
        this.transactionDate = transactionDate;
        this.value = value;
        this.currency = currency;
        this.responsible = responsible;
        this.account = account;
        this.card = card;
        this.owner = owner;
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

    public Client getResponsible() {
        return responsible;
    }

    public void setResponsible(final Client owner) {
        this.responsible = owner;
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
