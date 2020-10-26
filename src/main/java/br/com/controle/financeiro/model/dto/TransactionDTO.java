package br.com.controle.financeiro.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Locale;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.com.controle.financeiro.model.entity.BankAccount;
import br.com.controle.financeiro.model.entity.Card;
import br.com.controle.financeiro.model.entity.Client;
import br.com.controle.financeiro.model.entity.Transaction;
import br.com.controle.financeiro.model.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

public class TransactionDTO implements Serializable {

    private UUID expenseId;

    @NotBlank
    private String name;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime transactionDate = LocalDateTime.now();

    @NotNull
    private BigDecimal value = BigDecimal.ZERO;

    @NotNull
    private Currency currency = Currency.getInstance(new Locale("pt", "BR"));

    @NotNull
    private UUID client = UUID.randomUUID();

    private UUID account;

    private UUID card;

    public TransactionDTO() {
        super();
    }

    public TransactionDTO(UUID expenseId, @NotNull String name, @NotNull LocalDateTime transactionDate,
                          @NotNull BigDecimal value, @NotNull Currency currency, UUID client, UUID account, UUID card) {
        super();
        this.expenseId = expenseId;
        this.name = name;
        this.transactionDate = transactionDate;
        this.value = value;
        this.currency = currency;
        this.client = client;
        this.account = account;
        this.card = card;
    }

    public static TransactionDTO fromTransaction(Transaction t) {
        return new TransactionDTO(t.getId(), t.getName(), t.getTransactionDate(), t.getValue(), t.getCurrency(),
                                  t.getResponsible().getId(), t.getAccount().getId(), t.getCard().getId());
    }

    public Transaction toTransaction(Client client, BankAccount accountObj, Card cardObj, UserEntity owner) {
        return new Transaction(this.getId(), this.getName(), this.getTransactionDate(), this.getValue(),
                               this.getCurrency(), client, accountObj, cardObj, owner);
    }

    public void setCard(final UUID id) {
        this.card = id;
    }

    public UUID getCard() {
        return this.card;
    }

    public void setAccount(final UUID account) {
        this.account = account;
    }

    public UUID getAccount() {
        return this.account;
    }

    public BigDecimal getValue() {
        return value;
    }

    public UUID getClient() {
        return client;
    }

    public void setClient(final UUID client) {
        this.client = client;
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
        return expenseId;
    }

    public void setId(UUID expenseId) {
        this.expenseId = expenseId;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

}