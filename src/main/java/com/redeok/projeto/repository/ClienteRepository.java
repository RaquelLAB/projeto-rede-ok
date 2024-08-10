package com.redeok.projeto.repository;

import com.redeok.projeto.entity.Cliente;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

@ApplicationScoped
public class ClienteRepository {

    @Inject
    Jdbi jdbi;

    public void createTable() {
        jdbi.useHandle(handle -> handle.execute("CREATE TABLE IF NOT EXISTS cliente (id_cliente SERIAL PRIMARY KEY, nome VARCHAR, telefone VARCHAR, email VARCHAR, num_documento VARCHAR, tipo_documento VARCHAR, data_criacao DATE)"));
    }

    public void insert(Cliente cliente) {
        jdbi.useHandle(handle -> handle.createUpdate("INSERT INTO cliente (nome, telefone, email, num_documento, tipo_documento) VALUES (:nome, :telefone, :email, :numDocumento, :tipoDocumento::tipo_documento_enum)")
                .bindBean(cliente)
                .execute());
    }

    public Cliente findById(String numDocumento) {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT * FROM cliente WHERE num_documento = :numDocumento")
                .bind("numDocumento", numDocumento)
                .mapToBean(Cliente.class)
                .findOne()
                .orElse(null)
        );
    }

    public List<Cliente> listAll() {
        return jdbi.withHandle(handle -> handle.createQuery("SELECT nome, telefone, email, num_documento, tipo_documento, data_criacao FROM cliente")
                .mapToBean(Cliente.class)
                .list());
    }

    public boolean existsByNumDocumento(String numDocumento) {
        String query = "SELECT COUNT(*) FROM cliente WHERE num_documento = :numDocumento";
        return jdbi.withHandle(handle ->
                handle.createQuery(query)
                        .bind("numDocumento", numDocumento)
                        .mapTo(Integer.class)
                        .findOne()
                        .orElse(0) > 0);
    }

    public void update(Cliente cliente) {
        jdbi.useHandle(handle -> {
            handle.createUpdate("UPDATE cliente SET nome = :nome, telefone = :telefone, email = :email, num_documento = :numDocumento, tipo_documento = :tipoDocumento::tipo_documento_enum WHERE num_documento = :numDocumento")
                    .bind("nome", cliente.getNome())
                    .bind("telefone", cliente.getTelefone())
                    .bind("email", cliente.getEmail())
                    .bind("numDocumento", cliente.getNumDocumento())
                    .bind("tipoDocumento", cliente.getTipoDocumento())
                    .execute();
        });
    }
}
