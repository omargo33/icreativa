package com.aplicaciones13.placas.servicio;

import java.util.List;
import java.util.Date;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aplicaciones13.placas.jpa.model.PlacaEvento;
import com.aplicaciones13.placas.jpa.queries.PlacaEventoRepositorio;

import lombok.RequiredArgsConstructor;

/**
 * Clase para el servicio de eventos de placas
 * 
 * @author omargo33@gmail.com
 * @version 2023-08-20
 *
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PlacaEventoServicio {

    @Autowired
    private final PlacaEventoRepositorio placaEventoRepositorio;

    /**
     * Metodo para buscar un id por placa.
     * 
     * @param id
     */
    public List<PlacaEvento> findByIdPlaca(Integer idPlaca) {
        return placaEventoRepositorio.findByIdPlacas(idPlaca);
    }

    /**
     * Metodo crear la placa por evento.
     * 
     * @param idPlaca
     * @param descripcion
     * @param estado
     * 
     */
    public PlacaEvento crearPlacaEvento(int idPlaca, String descripcion, String estado) {
        PlacaEvento valor = new PlacaEvento();
        descripcion = descripcion.substring(0, Math.min(descripcion.length(), 128));
        valor.setDescripcion(descripcion);
        valor.setEstado(estado);
        valor.setFecha(new Date());
        valor.setIdPlacas(idPlaca);
        return placaEventoRepositorio.save(valor);
    }

    /** Metodo para buscar un evento con estado M 
     * 
    */
    public PlacaEvento findByIdPlacaAndEstadoM(Integer idPlaca) {
        return placaEventoRepositorio.findByIdPlacasAndEstadoM(idPlaca);
    }
}
