package com.redeok.projeto.validaton.validator;

import com.redeok.projeto.enums.EnumTipoDocumento;
import jakarta.ws.rs.core.Response;

public class PatchValidator {

     public Response validarNome(String nome) {
         if (nome.matches("^[A-Za-zÀ-ÖØ-öø-ÿ ]+$")) {
             return null;

         } else {
             return Response.status(Response.Status.BAD_REQUEST)
                     .entity("O nome deve conter apenas letras e espaços")
                     .build();
         }
     }

    public Response validarTelefone(String telefone) {
        if (telefone.matches("\\d{10,11}")) {
            return null;

        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Telefone inválido")
                    .build();
        }
    }

     public Response validarEmail(String email) {
         if(email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
             return null;
         } else {
             return Response.status(Response.Status.BAD_REQUEST)
                     .entity("Email inválido")
                     .build();
         }
     }

    public Response validarNumDocumento(String numDocumento) {
        if (numDocumento.matches("\\d{11,14}")) {
            return null;

        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Número de documento inválido")
                    .build();
        }
    }

    public Response validarTipoDocumento(String tipoDocumento) {
        if (tipoDocumento.equals(EnumTipoDocumento.CPF.getDescricao()) || tipoDocumento.equals(EnumTipoDocumento.CNPJ.getDescricao())) {
            return null;

        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Tipo de documento inválido.")
                    .build();
        }
    }

}
