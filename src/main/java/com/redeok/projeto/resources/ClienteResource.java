package com.redeok.projeto.resources;


import com.redeok.projeto.repositories.EnderecoRepository;
import com.redeok.projeto.validaton.validator.PatchValidator;
import com.redeok.projeto.domain.Cliente;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.redeok.projeto.repositories.ClienteRepository;
import com.redeok.projeto.dto.ClientePatchDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Path("/clientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClienteResource {


    @Inject
    ClienteRepository clienteRepository;

    @Inject
    EnderecoRepository enderecoRepository;

    @GET
    public List<Cliente> listAll() {
        return clienteRepository.listAll();
    }

    @GET
    @Path("/{numDocumento}")
    public Cliente procurarPorNumDocumento(@PathParam("numDocumento") String numDocumento) {
        return clienteRepository.findById(numDocumento);
    }

    @POST
    public Response create(@Valid Cliente cliente) {
        try {
            if (clienteRepository.existsByNumDocumento(cliente.getNumDocumento())) {
                return Response.status(Response.Status.CONFLICT)
                        .entity("Já existe cliente cadastrado com esse documento")
                        .build();
            }
            clienteRepository.insert(cliente);
            return Response.status(Response.Status.CREATED)
                    .entity("Cliente cadastrado com sucesso")
                    .build();
        } catch (ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getConstraintViolations().stream()
                            .map(v -> v.getMessage())
                            .collect(Collectors.joining(", ")))
                    .build();
        }

    }

    @PATCH
    @Path("/{numDocumento}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePartial(@PathParam("numDocumento") String numDocumento, ClientePatchDTO patchDto) {
        Cliente existingCliente = clienteRepository.findById(numDocumento);

        if (existingCliente == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        PatchValidator patchValidator = new PatchValidator();

        Map<Supplier<Optional<String>>, Function<String, Response>> validations = Map.of(
                patchDto::getNome, patchValidator::validarNome,
                patchDto::getTelefone, patchValidator::validarTelefone,
                patchDto::getEmail, patchValidator::validarEmail,
                patchDto::getNumDocumento, patchValidator::validarNumDocumento,
                patchDto::getTipoDocumento, patchValidator::validarTipoDocumento
        );

        for (Map.Entry<Supplier<Optional<String>>, Function<String, Response>> entry : validations.entrySet()) {
            Optional<String> valueOpt = entry.getKey().get();
            if (valueOpt.isPresent()) {
                Response erro = entry.getValue().apply(valueOpt.get());
                if (erro != null) {
                    return erro;
                }
            }
        }

        patchDto.getNome().ifPresent(existingCliente::setNome);
        patchDto.getTelefone().ifPresent(existingCliente::setTelefone);
        patchDto.getEmail().ifPresent(existingCliente::setEmail);
        patchDto.getNumDocumento().ifPresent(existingCliente::setNumDocumento);
        patchDto.getTipoDocumento().ifPresent(existingCliente::setTipoDocumento);

        clienteRepository.update(existingCliente);

        return Response.ok(existingCliente).build();
    }

    @Path("/{numDocumento}/enderecos")
    public EnderecoResource getEnderecoResource() {
        return new EnderecoResource(enderecoRepository, clienteRepository);
    }

}
