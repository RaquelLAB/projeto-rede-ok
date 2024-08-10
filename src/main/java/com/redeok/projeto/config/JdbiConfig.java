package com.redeok.projeto.config;

import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.jdbi.v3.core.Jdbi;

@ApplicationScoped
public class JdbiConfig {

    @Inject
    AgroalDataSource dataSource;

    @Produces
    @ApplicationScoped
    public Jdbi jdbi() {
        return Jdbi.create(dataSource);
    }
}
