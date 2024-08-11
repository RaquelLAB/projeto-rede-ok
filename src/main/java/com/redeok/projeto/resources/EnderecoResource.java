package com.redeok.projeto.resources;

import com.redeok.projeto.domain.Cliente;
import com.redeok.projeto.domain.Endereco;
import com.redeok.projeto.repositories.ClienteRepository;
import com.redeok.projeto.repositories.EnderecoRepository;
import com.redeok.projeto.validaton.validator.EnderecoValidator;
import jakarta.transaction.Transactional;


import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EnderecoResource {

    private EnderecoRepository enderecoRepository;
    private ClienteRepository clienteRepository;

    public EnderecoResource(EnderecoRepository enderecoRepository, ClienteRepository clienteRepository) {
        this.enderecoRepository = enderecoRepository;
        this.clienteRepository = clienteRepository;
    }

    @GET
    public List<Endereco> getEnderecos(@PathParam("numDocumento") String numDocumento) {
        Cliente existingCliente = clienteRepository.findById(numDocumento);
        return enderecoRepository.listarEnderecosPorCliente(existingCliente.getId());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createEndereco(@PathParam("numDocumento") String numDocumento, @Valid Endereco endereco) {

            EnderecoValidator enderecoValidator = new EnderecoValidator();

            Cliente existingCliente = clienteRepository.findById(numDocumento);

            if (existingCliente == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Cliente com o número de documento " + numDocumento + " não encontrado.")
                        .build();
            }

            Map<Supplier<String>, Function<String, Response>> validations = Map.of(
                    endereco::getCep, enderecoValidator::validarCep,
                    endereco::getUf, enderecoValidator::validarUf,
                    endereco::getCidade, enderecoValidator::validarCidade,
                    endereco::getLogradouro, enderecoValidator::validarLogradouro,
                    endereco::getNumero, enderecoValidator::validarNumero
            );

            for (Map.Entry<Supplier<String>, Function<String, Response>> entry : validations.entrySet()) {
                String value = entry.getKey().get();
                Response erro = entry.getValue().apply(value);
                if (erro != null) {
                    return erro;
                }
            }

            endereco.setIdCliente(existingCliente.getId());
            enderecoRepository.insert(endereco);
            return Response.status(Response.Status.CREATED)
                    .entity("Endereco cadastrado com sucesso")
                    .build();

    }

    @DELETE
    @Path("/{idEndereco}")
    public Response deleteEndereco(@PathParam("numDocumento") String numDocumento, @PathParam("idEndereco") Long idEndereco) {
        Cliente existingCliente = clienteRepository.findById(numDocumento);

        if (existingCliente == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Cliente com o número de documento " + numDocumento + " não encontrado.")
                    .build();
        }

        Endereco endereco = enderecoRepository.findById(idEndereco);

        if (endereco == null || !endereco.getIdCliente().equals(existingCliente.getId())) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Endereço não encontrado ou não pertence ao cliente especificado.")
                    .build();
        }

        enderecoRepository.delete(idEndereco);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
