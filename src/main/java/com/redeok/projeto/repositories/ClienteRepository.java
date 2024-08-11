package com.redeok.projeto.repositories;

import com.redeok.projeto.domain.Cliente;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;
import org.jdbi.v3.core.statement.Query;

import java.util.List;

@ApplicationScoped
public class ClienteRepository {

    @Inject
    Jdbi jdbi;

    public void createTable() {
        jdbi.useHandle(handle -> handle.execute(
                "DO $$ " +
                        "BEGIN " +
                        "    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'tipo_documento_enum') THEN " +
                        "        CREATE TYPE tipo_documento_enum AS ENUM ('CPF', 'CNPJ'); " +
                        "    END IF; " +
                        "END $$;" +
                        "CREATE TABLE IF NOT EXISTS cliente (" +
                        "    id_cliente SERIAL PRIMARY KEY, " +
                        "    nome VARCHAR(100), " +
                        "    telefone VARCHAR(11), " +
                        "    email VARCHAR(200) NOT NULL, " +
                        "    num_documento VARCHAR(14) NOT NULL, " +
                        "    tipo_documento tipo_documento_enum NOT NULL, " +
                        "    data_criacao DATE DEFAULT CURRENT_DATE, " +
                        "    CONSTRAINT email_valido CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$') " +
                        ");"
        ));
    }

    public void insert(Cliente cliente) {
        jdbi.useHandle(handle -> handle.createUpdate("INSERT INTO cliente (nome, telefone, email, num_documento, tipo_documento) VALUES (:nome, :telefone, :email, :numDocumento, :tipoDocumento::tipo_documento_enum)")
                .bindBean(cliente)
                .execute());
    }

    public Cliente findById(String numDocumento) {
        return jdbi.withHandle(handle -> {
            Query query = handle.createQuery("SELECT id_cliente, nome, telefone, email, num_documento, tipo_documento, data_criacao FROM cliente WHERE num_documento = :numDocumento");
            query.bind("numDocumento", numDocumento);
            query.registerRowMapper(ConstructorMapper.factory(Cliente.class));
            return query.mapTo(Cliente.class).findOne().orElse(null);
        });
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
