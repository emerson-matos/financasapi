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
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

public class TransactionDTO implements Serializable {

    private Long expenseId;

    @NotBlank
    private String name;

    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime transactionDate = LocalDateTime.now();

    @NotNull
    private BigDecimal value = BigDecimal.ZERO;

    @NotNull
    private Currency currency = Currency.getInstance(new Locale("pt", "BR"));

    @NotNull
    private UUID owner;

    private Long account;

    private Long card;

    public TransactionDTO() {
        super();
    }

    public TransactionDTO(Long expenseId, @NotNull String name, @NotNull LocalDateTime transactionDate,
                          @NotNull BigDecimal value, @NotNull Currency currency, UUID owner, Long account,
                          Long card) {
        super();
        this.expenseId = expenseId;
        this.name = name;
        this.transactionDate = transactionDate;
        this.value = value;
        this.currency = currency;
        this.owner = owner;
        this.account = account;
        this.card = card;
    }

    public static TransactionDTO fromTransaction(Transaction t) {
        return new TransactionDTO(t.getId(), t.getName(), t.getTransactionDate(), t.getValue(), t.getCurrency(),
                                  t.getOwner().getId(), t.getAccount().getId(), t.getCard().getId());
    }

    public Transaction toTransaction(Client owner, BankAccount accountObj, Card cardObj) {
        return new Transaction(this.getId(), this.getName(), this.getTransactionDate(), this.getValue(),
                               this.getCurrency(), owner, accountObj, cardObj);
    }

    public void setCard(final Long id) {
        this.card = id;
    }

    public Long getCard() {
        return this.card;
    }

    public void setAccount(final Long account) {
        this.account = account;
    }

    public Long getAccount() {
        return this.account;
    }

    public BigDecimal getValue() {
        return value;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(final UUID owner) {
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