package com.aplicaciones13.placas.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.aplicaciones13.placas.servicio.ConsultarPlacaServicio;

/**
 * Clase para consultar placas mediante un scheduler.
 * 
 * @autor: omargo33@gmail.com
 * @since: 2023-08-13
 * 
 */
@Component
public class ConsultarPlaca {

    @Autowired
    private ConsultarPlacaServicio consultarPlacaServicio;

    //@Scheduled(cron = "0 0/30 * * * 0-6", zone = "GMT-5")
    public void ejecutarREST() {
        consultarPlacaServicio.consultarPlacasEncontradas("C");
    }

    //@Scheduled(cron = "0 0/50 * * * *", zone = "GMT-5")
    public void ejecutarWeb() {
        consultarPlacaServicio.consultarPlacasEncontradas("E");
    }    

    //@Scheduled(cron = "0 0/90 * * * *", zone = "GMT-5")
    public void ejecutarMail() {
        consultarPlacaServicio.consultarPlacasEncontradas("M");
    }
}
