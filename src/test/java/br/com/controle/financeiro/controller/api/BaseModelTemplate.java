package br.com.controle.financeiro.controller.api;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Currency;
import java.util.UUID;

import br.com.controle.financeiro.configuration.security.SecurityConfig;
import br.com.controle.financeiro.model.entity.BankAccount;
import br.com.controle.financeiro.model.entity.Card;
import br.com.controle.financeiro.model.entity.Client;
import br.com.controle.financeiro.model.entity.Institution;
import br.com.controle.financeiro.model.entity.Role;
import br.com.controle.financeiro.model.entity.Transaction;
import br.com.controle.financeiro.model.entity.UserEntity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BaseModelTemplate {

    BankAccount bankAccount;
    Card card;
    Client client;
    Institution institution;
    Transaction transaction;
    UserEntity owner;

    void setupModel() {
        setupOwner();
        setupInstitution();
        setupClient();
        setupCard();
        setupBankAccount();
        setupTransaction();
    }

    private void setupTransaction() {
        transaction = new Transaction(UUID.randomUUID(), "name", LocalDateTime.now(), new BigDecimal(1),
                                      Currency.getInstance("BRL"), new Client(), new BankAccount(), new Card(), owner);
    }

    private void setupInstitution() {
        institution = new Institution(UUID.randomUUID(), "identifier", "name");
    }

    private void setupCard() {
        card = new Card(UUID.randomUUID(), "card", "5432", client, institution, owner);
    }

    private void setupClient() {
        client = new Client(UUID.randomUUID(), "name", owner);
    }

    private void setupOwner() {
        owner = new UserEntity();
        owner.setName("owner");
        owner.setPassword(new BCryptPasswordEncoder().encode("owner"));
        owner.setEmail("some@one.com");
        owner.setId(UUID.randomUUID().toString());
        owner.setAuthorities(Collections.singletonList(new Role(SecurityConfig.Roles.ROLE_ADMIN)));
    }

    private void setupBankAccount() {
        bankAccount = new BankAccount(UUID.randomUUID(), "5432", "543", "8", client, institution, owner);
    }

}
