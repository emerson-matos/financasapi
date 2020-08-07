package br.com.controle.financeiro.model.dto;

import java.io.Serializable;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.com.controle.financeiro.model.entity.BankAccount;
import br.com.controle.financeiro.model.entity.Client;
import br.com.controle.financeiro.model.entity.Institution;
import br.com.controle.financeiro.model.entity.UserEntity;

public class BankAccountDTO implements Serializable {

    private UUID accountId;

    @NotBlank
    private String agency;

    @NotBlank
    private String number;

    @NotBlank
    private String dac;

    @NotNull
    private UUID responsible = UUID.randomUUID();

    @NotNull
    private UUID institution = UUID.randomUUID();


    public BankAccountDTO() {
        super();
    }

    public BankAccountDTO(UUID id, String agency, String number, String dac, UUID responsible,
                          UUID institution) {
        super();
        this.accountId = id;
        this.agency = agency;
        this.number = number;
        this.dac = dac;
        this.responsible = responsible;
        this.institution = institution;
    }

    public static BankAccountDTO fromBankAccount(BankAccount account) {
        return new BankAccountDTO(account.getId(), account.getAgency(), account.getNumber(), account.getDac(),
                                  account.getResponsible().getId(), account.getInstitution().getId());
    }

    public BankAccount toBankAccount(Client responsible, Institution institution, UserEntity owner) {
        return new BankAccount(this.accountId, this.agency, this.number, this.dac, responsible, institution, owner);
    }

    public UUID getId() {
        return this.accountId;
    }

    public void setId(final UUID id) {
        this.accountId = id;
    }

    public String getAgency() {
        return this.agency;
    }

    public void setAgency(final String agency) {
        this.agency = agency;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(final String number) {
        this.number = number;
    }

    public String getDac() {
        return this.dac;
    }

    public void setDac(final String dac) {
        this.dac = dac;
    }

    public UUID getResponsible() {
        return this.responsible;
    }

    public void setOwner(final UUID owner) {
        this.responsible = owner;
    }

    public UUID getInstitution() {
        return this.institution;
    }

    public void setInstitution(final UUID institution) {
        this.institution = institution;
    }

}
