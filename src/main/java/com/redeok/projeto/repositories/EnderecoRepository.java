package com.redeok.projeto.repositories;

import com.redeok.projeto.domain.Endereco;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper;
import org.jdbi.v3.core.statement.Query;

import java.util.List;

@ApplicationScoped
public class EnderecoRepository {

    @Inject
    Jdbi jdbi;

    public void createTable() {
        jdbi.useHandle(handle -> handle.execute("CREATE TABLE IF NOT EXISTS endereco ("+
                "    id_endereco SERIAL PRIMARY KEY," +
                "    cliente INT NOT NULL," +
                "    CEP VARCHAR(10) NOT NULL," +
                "    UF VARCHAR(2) NOT NULL," +
                "    cidade VARCHAR(50) NOT NULL," +
                "    logradouro VARCHAR(100) NOT NULL," +
                "    numero VARCHAR(10) NOT NULL," +
                "    FOREIGN KEY (cliente) REFERENCES cliente(id_cliente)" +
                ")"));
    }

    public Endereco findById(Long id) {
        return jdbi.withHandle(handle -> {
            Query query = handle.createQuery("SELECT * FROM endereco WHERE id_endereco = :id");
            query.bind("id", id);
            query.registerRowMapper(ConstructorMapper.factory(Endereco.class));
            return query.mapTo(Endereco.class).findOne().orElse(null);
        });
    }

    public void insert(Endereco endereco) {
        jdbi.useHandle(handle ->
                handle.createUpdate("INSERT INTO endereco (cliente, cep, uf, cidade, logradouro, numero) VALUES (:idCliente, :cep, :uf, :cidade, :logradouro, :numero)")
                        .bindBean(endereco)
                        .execute()
        );
    }

    public List<Endereco> listarEnderecosPorCliente(Long clienteId) {
        return jdbi.withHandle(handle ->
                handle.createQuery("SELECT * FROM endereco WHERE cliente = :clienteId")
                        .bind("clienteId", clienteId)
                        .mapToBean(Endereco.class)
                        .list()
        );
    }

    public void delete(Long id) {
        jdbi.useHandle(handle ->
                handle.createUpdate("DELETE FROM endereco WHERE id_endereco = :id")
                        .bind("id", id)
                        .execute()
        );
    }
}
