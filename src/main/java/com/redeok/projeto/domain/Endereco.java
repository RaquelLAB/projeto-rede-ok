package com.redeok.projeto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.beans.ConstructorProperties;

public class Endereco {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long idCliente;

    @Pattern(regexp = "\\d{8}", message = "CEP inválido")
    @NotBlank(message = "CEP é obrigatório")
    private String cep;

    @Pattern(regexp = "^[A-Za-z]{2}$", message = "UF inválida")
    @NotBlank(message = "UF é obrigatória")
    private String uf;

    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ ]+$", message = "Nome da cidade é inválido")
    @NotBlank(message = "Campo cidade é obrigatório")
    private String cidade;

    @NotBlank(message = "Campo logradouro é obrigatório")
    private String logradouro;

    @Pattern(regexp = "\\d", message = "Campo número inválido")
    @NotBlank(message = "Campo número é obrigatório")
    private String numero;

    @ConstructorProperties({"id_endereco", "cliente", "cep", "uf", "cidade", "logradouro", "numero"})
    public Endereco(Long id, Long cliente, String cep, String uf, String cidade, String logradouro, String numero) {
        this.id = id;
        this.idCliente = cliente;
        this.cep = cep;
        this.uf = uf;
        this.cidade = cidade;
        this.logradouro = logradouro;
        this.numero = numero;
    }

    public Endereco() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
