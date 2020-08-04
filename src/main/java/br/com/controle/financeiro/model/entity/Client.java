package br.com.controle.financeiro.model.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity(name = "client")
public class Client extends AbstractPersistable<UUID> implements Serializable {

    private String name;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private UserEntity owner;

    public Client() {
        super();
    }

    public Client(final UUID id, final String name, final UserEntity owner) {
        super();
        this.setId(id);
        this.name = name;
        this.owner = owner;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public Client withId(UUID id) {
        this.setId(id);
        return this;
    }

}
