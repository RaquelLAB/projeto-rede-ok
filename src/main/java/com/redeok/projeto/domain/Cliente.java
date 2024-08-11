package com.redeok.projeto.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.redeok.projeto.validaton.annotation.ValidNome;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import com.redeok.projeto.validaton.annotation.ValidEnumTipoDocumento;
import com.redeok.projeto.validaton.annotation.ValidTelefone;
import jakarta.validation.constraints.Pattern;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

import java.beans.ConstructorProperties;
import java.time.LocalDate;


public class Cliente {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    @ValidNome
    private String nome;

    @ValidTelefone
    private String telefone;

    @Email(message = "Email deve ser válido")
    @NotBlank(message = "Email é obrigatório")
    private String email;

    @Pattern(regexp = "\\d{11,14}", message = "Número de documento inválido")
    @NotBlank(message = "Número de documento é obrigatório")
    private String numDocumento;

    @ValidEnumTipoDocumento
    @NotBlank(message = "Tipo de documento é obrigatório")
    private String tipoDocumento;

    @ColumnName("data_criacao")
    private LocalDate dataCriacao;

    @ConstructorProperties({"id_cliente", "nome", "telefone", "email", "numDocumento", "tipoDocumento", "dataCriacao"})
    public Cliente (Long id_cliente, String nome, String telefone, String email, String numDocumento, String tipoDocumento, LocalDate dataCriacao) {
        this.id = id_cliente;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.numDocumento = numDocumento;
        this.tipoDocumento = tipoDocumento;
        this.dataCriacao = dataCriacao;
    }

    public Cliente () {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
