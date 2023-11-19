package com.aplicaciones13.downride.cliente.utilidades;

import lombok.Getter;
import lombok.Setter;

/**
 * Clase para consultar un ride
 * 
 * @author omargo33@hotmail.com
 * @since 2023-10-21
 * 
 */
@Getter
@Setter
public class ConsultaRide {
    String dia;
    String mes;
    String anio;

    /**
     * Constructor
     * 
     * @param dia
     * @param mes
     * @param anio
     */
    public ConsultaRide(String dia, String mes, String anio) {
        this.dia = dia;
        this.mes = mes;
        this.anio = anio;
    }
}
