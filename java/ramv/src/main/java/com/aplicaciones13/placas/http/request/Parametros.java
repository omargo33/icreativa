package com.aplicaciones13.placas.http.request;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aplicaciones13.placas.jpa.model.Parametro;
import com.aplicaciones13.placas.servicio.ParametroServicio;

import io.swagger.v3.oas.annotations.Hidden;

/**
 * Clase para consumir el servicio de parametros.
 * 
 * @author omargo33@gmail.com
 * @since 2023-08-20
 */
@Hidden
@RestController
@RequestMapping(value = "/v1/parametros")
public class Parametros extends ComonControlador {

    @Autowired
    ParametroServicio parametroServicio;

    @GetMapping("/{id}")
    public Parametro getParametro(@PathVariable Integer id) {
        return parametroServicio.findByIdParametros(id);
    }

    @PutMapping("/")
    public Parametro putParametro(@Valid @RequestBody Parametro valor) {
        return parametroServicio.updateParametro(valor);
    }
}
