package br.com.controle.financeiro.model.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Transacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idDespesa;

    private String nome;

    private Date dataTransacao;
    private double valor;

    @ManyToOne
    @JoinColumn(name = "idResponsavel")
    private Client responsavel;

    @ManyToOne
    @JoinColumn(name = "idConta")
    private ContaBancaria conta;

    @ManyToOne
    @JoinColumn(name = "idCartao")
    private Cartao cartao;

    public Transacao() {
        super();
    }

    public Transacao(double valor, String nome, Date data, Client responsavel, ContaBancaria conta, Cartao cartao) {
        super();
        this.setNome(nome);
        this.setDataTransacao(data);
        this.setResponsavel(responsavel);
        this.setValor(valor);
        this.setContaBancaria(conta);
        this.setCartao(cartao);
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    public Cartao getCartao() {
        return this.cartao;
    }

    public void setContaBancaria(ContaBancaria conta) {
        this.conta = conta;
    }

    public ContaBancaria getContaBancaria() {
        return this.conta;
    }

    public double getValor() {
        return valor;
    }

    public Client getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Client responsavel) {
        this.responsavel = responsavel;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Date getDataTransacao() {
        return dataTransacao;
    }

    public void setDataTransacao(Date dataTransacao) {
        this.dataTransacao = dataTransacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
