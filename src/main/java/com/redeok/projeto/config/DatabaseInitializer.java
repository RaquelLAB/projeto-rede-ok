package com.redeok.projeto.config;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import com.redeok.projeto.repository.ClienteRepository;

@ApplicationScoped
public class DatabaseInitializer {

    @Inject
    ClienteRepository clienteRepository;

    void onStart(@Observes StartupEvent ev) {
        clienteRepository.createTable();
    }
}
