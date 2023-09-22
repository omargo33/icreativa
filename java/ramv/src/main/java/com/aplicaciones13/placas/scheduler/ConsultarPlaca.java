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

    @Scheduled(cron = "0 0/30 * * * 0-6", zone = "GMT-5")
    public void ejecutarREST() {
        consultarPlacaServicio.consultarPlacasEncontradas("C");
    }

    /**
     * Metodo para consultar las placas encontradas en la base de datos.
     * 
     * Cada 45 minutos, de 8am a 7pm, de lunes a sabado.
     */
    @Scheduled(cron = "0 */45 8-19 * * MON-SAT", zone = "GMT-5")
    public void ejecutarWeb() {
        consultarPlacaServicio.consultarPlacasEncontradas("E");
    }    

    @Scheduled(cron = "0 0/90 * * * *", zone = "GMT-5")
    public void ejecutarMail() {
        consultarPlacaServicio.consultarPlacasEncontradas("M");
    }
}
