package br.com.controle.financeiro.model.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class ContaBancaria implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long idConta;

	private String agencia;
	private String numero;
	private String dac;
	private Client dono;

	@OneToOne
	private Instituicao instituicao;

	public ContaBancaria() {
		super();
	}

	public ContaBancaria(Client dono, Instituicao instituicao) {
		super();
		this.dono = dono;
		this.instituicao = instituicao;
	}

	public Client getDono() {
		return dono;
	}

	public void setDono(Client dono) {
		this.dono = dono;
	}

	public Instituicao getInstituicao() {
		return instituicao;
	}

	public void setInstituicao(Instituicao instituicao) {
		this.instituicao = instituicao;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getDac() {
		return dac;
	}

	public void setDac(String dac) {
		this.dac = dac;
	}

}
