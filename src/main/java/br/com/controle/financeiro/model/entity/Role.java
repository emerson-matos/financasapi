package br.com.controle.financeiro.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

@Entity(name = "role")
public class Role implements GrantedAuthority {

    @Id
    @Column(name = "id_role")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long roleId;

    @Column(name = "authority_")
    private String authority;

    public Role() {
    }

    public Role(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setRoleId(Long id) {
        this.roleId = id;
    }

    public Long getRoleId() {
        return roleId;
    }

}