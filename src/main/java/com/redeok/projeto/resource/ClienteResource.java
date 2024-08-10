package com.redeok.projeto.resource;




import com.redeok.projeto.entity.Cliente;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.redeok.projeto.repository.ClienteRepository;

import java.util.List;
import java.util.stream.Collectors;

@Path("/clientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClienteResource {


    @Inject
    ClienteRepository clienteRepository;

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
}
