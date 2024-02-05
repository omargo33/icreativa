package com.aplicaciones13.downride.servicio;

import java.time.LocalDate;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aplicaciones13.downride.jpa.model.Dia;
import com.aplicaciones13.downride.jpa.queries.DiaRepositorio;

import lombok.RequiredArgsConstructor;

/**
 * Clase para el servicio de los dias a consultar.
 * 
 * @author omargo33@gmail.com
 * @version 2023-11-26
 */
@Service
@Transactional
@RequiredArgsConstructor
public class DiaServicio {

    @Autowired
    private final DiaRepositorio diaRepositorio;

    /**
     * Metodo para validar que la fecha sea correcta.
     * 
     * El formatos de fecha adminitodos estan el la clase ConsumoWebCliente.java
     * 
     * @param dia
     * @param mes
     * @param anio
     * @return
     */
    private boolean validarFecha(String dia, String mes, String anio) {        
        int currentYear = LocalDate.now().getYear();

        // Validar que el anio sea un numero
        try {
            int anioNumero = Integer.parseInt(anio);
            if (anioNumero < 2022 || anioNumero > currentYear + 1) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        // Validar que el dia sea un numero
        try {
            int diaNumero = Integer.parseInt(dia);
            if (diaNumero < 1 || diaNumero > 31) {
                return false;
            }
        } catch (NumberFormatException e) {
            if (!dia.equals("Todos")) {
                return false;
            }
        }

        // Validar que el mes sea un mes
        switch (mes) {
            case "Enero":
            case "Febrero":
            case "Marzo":
            case "Abril":
            case "Mayo":
            case "Junio":
            case "Julio":
            case "Agosto":
            case "Septiembre":
            case "Octubre":
            case "Noviembre":
            case "Diciembre":
                break;
            default:
                return false;
        }

        return true;
    }

    /**
     * Metodo para crear una solicitud de dia.
     * 
     * @param valor
     */
    public Dia crearDia(Dia valor) {
        valor.setUsuarioFecha(new Date());

        // Validar que el dia sea un numero
        if (validarFecha(valor.getDia(), valor.getMes(), valor.getAnio())) {
            valor.setEstado("P");
            valor.setObservacion("Solicitud creada");
            return diaRepositorio.save(valor);
        }
        valor.setEstado("X");
        valor.setObservacion("Formato de fecha incorrecto ");
        return diaRepositorio.save(valor);
    }
}
