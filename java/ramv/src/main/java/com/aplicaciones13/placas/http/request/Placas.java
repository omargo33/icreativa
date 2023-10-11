package com.aplicaciones13.placas.http.request;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aplicaciones13.placas.jpa.model.Placa;
import com.aplicaciones13.placas.jpa.model.PlacaEvento;
import com.aplicaciones13.placas.servicio.PlacaEventoServicio;
import com.aplicaciones13.placas.servicio.PlacaServicio;

/**
 * Clase que contiene los metodos para ingreso de placas a consultar y respuestas que el sistema tiene al respecto.
 * 
 * @autor: omargo33@gmail.com
 * @since: 2023-08-13
 * 
 */
@RestController
@RequestMapping(value = "/placas")
public class Placas {

    @Autowired
    private PlacaServicio placaServicio;

    @Autowired
    private PlacaEventoServicio placaEventoServicio;

    @GetMapping("/{placa}")
    public Placa getPlaca(@PathVariable String placa) {
        return placaServicio.findByPlaca(placa);
    }

    @GetMapping("/eventos/{placa}")
    public List<PlacaEvento> getPlacaEventos(@PathVariable String placa) {
        Placa placaConsulta = placaServicio.findByPlaca(placa);

        return placaEventoServicio.findByIdPlaca(placaConsulta.getIdPlacas());
    }

    @PostMapping("/")
    public Placa postPlaca(@Valid @RequestBody Placa valor) {
        return placaServicio.crearPlaca(valor);
    }
}
