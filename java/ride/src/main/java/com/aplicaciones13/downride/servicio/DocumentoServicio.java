package com.aplicaciones13.downride.servicio;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aplicaciones13.downride.jpa.model.Documento;
import com.aplicaciones13.downride.jpa.queries.DocumentoRepositorio;

import lombok.RequiredArgsConstructor;

/**
 * Clase para el servicio de los parametros
 * 
 * @author omargo33@gmail.com
 * @version 2023-08-20
 *
 */
@Service
@Transactional
@RequiredArgsConstructor
public class DocumentoServicio {

    @Autowired
    private final DocumentoRepositorio documentoRepositorio;

    /**
     * Metodo para buscar por RUC y una fecha en particular.
     * 
     * @param identificacionReceptor
     * @param fechaAutorizacion
     * @return
     */
    public List<Documento> findByIdentificacionReceptorAndFechaAutorizacion(String identificacionReceptor,
            Date fechaAutorizacion) {
        return documentoRepositorio.findByIdentificacionReceptorAndFechaAutorizacion(identificacionReceptor,
                fechaAutorizacion);
    }

    /**
     * Metodo para buscar por RUC y un rango de fechas.
     * 
     * @param identificacionReceptor
     * @param fechaAutorizacionInicio
     * @param fechaAutorizacionFin
     * @return
     */
    public List<Documento> findByIdentificacionReceptorAndFechaAutorizacionRango(String identificacionReceptor,
            Date fechaAutorizacionInicio, Date fechaAutorizacionFin) {
        return documentoRepositorio.findByIdentificacionReceptorAndFechaAutorizacionBetween(identificacionReceptor,
                fechaAutorizacionInicio, fechaAutorizacionFin);
    }
}
