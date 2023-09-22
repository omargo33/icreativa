package com.aplicaciones13.placas.servicio;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aplicaciones13.placas.ConstantesPlacas;
import com.aplicaciones13.placas.jpa.model.Placa;
import com.aplicaciones13.placas.jpa.queries.PlacaRepositorio;

import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Clase para el servicio placa
 * 
 * @author omargo33@gmail.com
 * @version 2023-08-20
 *
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PlacaServicio {

    @Autowired
    private final PlacaRepositorio placaRepositorio;

    @Autowired
    private final PlacaEventoServicio placaEventoServicio;

    /**
     * Metodo para buscar un id por placa.
     * 
     * @param id
     */
    public Placa findByPlaca(String placa) {
        return placaRepositorio.findByPlaca(placa);
    }

    /**
     * Metodo para buscar un id por placa.
     * 
     * @param id
     */
    public List<Placa> findByEstado(String estado) {
        return placaRepositorio.findByEstado(estado);
    }

    /**
     * Metodo para crear una placa.
     * 
     * @param valor 
     */
    public Placa crearPlaca(Placa valor) {
        Placa placa = placaRepositorio.findByPlaca(valor.getRamvPlaca());

        if (placa != null) {
            placaEventoServicio.crearPlacaEvento(placa.getIdPlacas(),
                    "Placa vuelta a ingresar por " + valor.getUsuario(), ConstantesPlacas.PLACA_EVENTO_REINGRESO);

            return placa;
        }

        valor.setUsuarioFecha(new Date());
        placa = placaRepositorio.save(valor);
        placaEventoServicio.crearPlacaEvento(placa.getIdPlacas(), "Placa creada", ConstantesPlacas.PLACA_EVENTO_CREAR);

        return placa;
    }

    /**
     * Metodo para actualizar un estado de una  placa
     * 
     * @param idPlacas
     * @param estado
     */
    public void actualizarEstado(Integer idPlacas, String estado) {
        placaRepositorio.actualizarEstado(idPlacas, estado);
    }
}
