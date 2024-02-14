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

    /**
     * Metodo para consultar las placas encontradas en la base de datos.
     * 
     * Cada 30 minutos, de lunes a sabado.
     */
    @Scheduled(cron = "0 0/30 * * * 0-5", zone = "GMT-5")
    public void ejecutarREST() {
        consultarPlacaServicio.consultarPlacasEncontradas("C");
    }

    /**
     * Metodo para consultar las placas encontradas en la base de datos.
     * 
     * Cada 45 minutos, de 8am a 7pm, de lunes a sabado.
     */
    @Scheduled(cron = "0 */45 8-19 * * MON-SAT", zone = "GMT-5")
    //@Scheduled(cron = "0 0/10 * * * *", zone = "GMT-5")
    //@Scheduled(cron = "0 */30 8-19 * * MON-SAT", zone = "GMT-5")
    public void ejecutarWeb() {
        consultarPlacaServicio.consultarPlacasEncontradas("E");
    }    

    /**
     * Metodo para consultar las placas encontradas en la base de datos.
     * 
     * Cada 90 minutos, de lunes a domingo.
     */
    @Scheduled(cron = "0 0/90 * * * *", zone = "GMT-5")
    public void ejecutarMail() {
        consultarPlacaServicio.consultarPlacasEncontradas("M");
    }

    /**
     * Metodo para consultar las placas muy antiguas.
     * 
     * Todos los dias a las 10am.
     */
    @Scheduled(cron = "0 0 10 * * *", zone = "GMT-5")
    public void ejecutarPlacasAntiguas() {
        consultarPlacaServicio.consumirPlacaAntiguas();
    }
}
