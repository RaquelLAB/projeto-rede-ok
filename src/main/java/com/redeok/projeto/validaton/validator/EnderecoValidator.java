package com.redeok.projeto.validaton.validator;

import jakarta.ws.rs.core.Response;

public class EnderecoValidator {

    public Response validarCep(String cep) {
        if (cep.matches("\\d{8}")) {
            return null;
        } else if (cep.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Campo CEP é obrigatório")
                    .build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("CEP inválido")
                    .build();
        }
    }

    public Response validarUf(String uf) {
        if (uf.matches("^[a-zA-Z]{2}$")) {
            return null;
        } else if (uf.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Campo UF é obrigatório")
                    .build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("UF inválida")
                    .build();
        }
    }

    public Response validarCidade(String cidade) {
        if (cidade.matches("^[A-Za-zÀ-ÖØ-öø-ÿ ]+$")) {
            return null;
        } else if (cidade.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Campo cidade é obrigatório")
                    .build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Cidade inválida")
                    .build();
        }
    }

    public Response validarLogradouro(String logradouro) {
        if (logradouro.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Campo logradouro é obrigatório")
                    .build();
        } else {
            return null;
        }
    }

    public Response validarNumero(String numero) {
        if (numero.matches("^\\d+$")) {
            return null;
        } else if (numero.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Campo número é obrigatório")
                    .build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Campo número inválido")
                    .build();
        }
    }
}
