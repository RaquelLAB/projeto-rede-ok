package com.redeok.projeto.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.redeok.projeto.Validaton.annotation.ValidNome;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import com.redeok.projeto.Validaton.annotation.ValidEnumTipoDocumento;
import com.redeok.projeto.Validaton.annotation.ValidTelefone;
import jakarta.validation.constraints.Pattern;

import java.util.Date;

public class Cliente {

    @JsonIgnore
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

    private Date dataCriacao;


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

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
