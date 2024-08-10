package com.redeok.projeto.enums;

public enum EnumTipoDocumento {

    CPF("CPF"),
    CNPJ("CNPJ");

    private String descricao;

    private EnumTipoDocumento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
