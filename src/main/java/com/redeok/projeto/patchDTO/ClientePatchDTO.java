package com.redeok.projeto.patchDTO;

import java.util.Optional;

public class ClientePatchDTO {

    private Optional<String> nome = Optional.empty();
    private Optional<String> telefone = Optional.empty();
    private Optional<String> email = Optional.empty();
    private Optional<String> numDocumento = Optional.empty();
    private Optional<String> tipoDocumento = Optional.empty();

    public Optional<String> getNome() {
        return nome;
    }

    public void setNome(Optional<String> nome) {
        this.nome = nome;
    }

    public Optional<String> getTelefone() {
        return telefone;
    }

    public void setTelefone(Optional<String> telefone) {
        this.telefone = telefone;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public void setEmail(Optional<String> email) {
        this.email = email;
    }

    public Optional<String> getNumDocumento() {
        return numDocumento;
    }

    public void setNumDocumento(Optional<String> numDocumento) {
        this.numDocumento = numDocumento;
    }

    public Optional<String> getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(Optional<String> tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }
}
