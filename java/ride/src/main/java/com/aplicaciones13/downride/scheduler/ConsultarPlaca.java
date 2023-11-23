package com.aplicaciones13.downride.scheduler;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.aplicaciones13.downride.servicio.RucServicio;

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
    RucServicio rucServicio;

    /**
     * Metodo para ejecutar el login de un ruc.
     * 
     * Cada 30 minutos, de lunes a sabado.
     */
    @Scheduled(cron = "0 0/30 * * * 0-5", zone = "GMT-5")
    public void ejecutarREST() {
        
    }

}
