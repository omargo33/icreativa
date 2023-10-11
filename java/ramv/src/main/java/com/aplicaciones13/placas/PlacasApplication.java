package com.aplicaciones13.placas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Clase para crear la aplicacion
 * 
 * @version 2023-08-15
 */
@ComponentScan(basePackages = {
        "com.aplicaciones13.placas.http.request",
        "com.aplicaciones13.placas.servicio",
        "com.aplicaciones13.placas.scheduler",
        // "com.aplicaciones13.placas.validadores",
        "com.aplicaciones13.placas.configuracion",
        "com.aplicaciones13.placas.security",
})
@EntityScan(basePackages = {
        "com.aplicaciones13.placas.jpa.model",
})

@EnableJpaRepositories(basePackages = {
        "com.aplicaciones13.placas.jpa.queries",
})

@SpringBootApplication
@EnableScheduling
public class PlacasApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlacasApplication.class, args);
    }

}
