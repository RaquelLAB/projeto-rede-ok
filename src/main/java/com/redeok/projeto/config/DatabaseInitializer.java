package com.redeok.projeto.config;

import com.redeok.projeto.repositories.EnderecoRepository;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import com.redeok.projeto.repositories.ClienteRepository;

@ApplicationScoped
public class DatabaseInitializer {

    @Inject
    ClienteRepository clienteRepository;

    @Inject
    EnderecoRepository enderecoRepository;

    void onStart(@Observes StartupEvent ev) {
        clienteRepository.createTable();
        enderecoRepository.createTable();
    }
}
