package br.com.controle.financeiro.model.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity(name = "institution")
public class Institution extends AbstractPersistable<UUID> implements Serializable {

    private String name;

    private String identifier;

    public Institution() {
        super();
    }

    public Institution(final UUID id, final String identifier, final String name) {
        super();
        this.setId(id);
        this.identifier = identifier;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }

}
