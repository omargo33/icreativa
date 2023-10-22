package com.aplicaciones13.downride.http.request;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aplicaciones13.downride.jpa.model.Parametro;
import com.aplicaciones13.downride.servicio.ParametroServicio;

import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Clase para consumir el servicio de parametros.
 * 
 * @author omargo33@gmail.com
 * @since 2023-08-20
 */
@RestController
@Tag(name = "parametros", description = "Servicio conocer los parametros de la aplicacion y actualizar los valores")
@RequestMapping(value = "/v1/parametros")
public class Parametros extends ComonControlador {

    @Autowired
    ParametroServicio parametroServicio;

    /**
     * Metodo para buscar todos los parametros
     * 
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Parametro getParametro(@PathVariable Integer id) {
        return parametroServicio.findByIdParametros(id);
    }

    /**
     * Metodo para actualizar un parametro
     * 
     * @param parametro
     * @return
     */
    @PutMapping("/")
    public Parametro putParametro(@Valid @RequestBody Parametro parametro) {
        return parametroServicio.updateParametro(parametro);
    }
}
