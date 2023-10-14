package com.aplicaciones13.downride;

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
                "com.aplicaciones13.downride.http.request",
                "com.aplicaciones13.downride.servicio",
                "com.aplicaciones13.downride.scheduler",
                // "com.aplicaciones13.downride.validadores",
                "com.aplicaciones13.downride.configuracion",
                // "com.aplicaciones13.downride.security",
})
@EntityScan(basePackages = {
                "com.aplicaciones13.downride.jpa.model",
})

@EnableJpaRepositories(basePackages = {
                "com.aplicaciones13.downride.jpa.queries",
})

@SpringBootApplication
@EnableScheduling
public class RideApplication {

        public static void main(String[] args) {
                SpringApplication.run(RideApplication.class, args);
        }

}
