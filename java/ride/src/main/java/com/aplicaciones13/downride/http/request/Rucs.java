package com.aplicaciones13.downride.http.request;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aplicaciones13.downride.jpa.model.Ruc;
import com.aplicaciones13.downride.servicio.RucServicio;

import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Clase para consumir el servicio de parametros.
 * 
 * @author omargo33@gmail.com
 * @since 2023-08-20
 */
@RestController
@Tag(name = "rucs", description = "Servicio de manejo de rucs")
@RequestMapping(value = "/v1/rucs")
public class Rucs extends ComonControlador {

    @Autowired
    RucServicio rucServicio;

    /**
     * Metodo para obtener todos los rucs
     * 
     * @return
     */
    @GetMapping("/")
    public List<Ruc> getRucs() {
        return rucServicio.getRucs();
    }

    /**
     * Metodo para obtener un ruc por id
     * 
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Ruc getRuc(@PathVariable Integer id) {
        return rucServicio.findByIdRucs(id);
    }

    /**
     * Metodo para obtener por ruc
     * 
     * @param ruc
     * @return
     */
    @GetMapping("/ruc={ruc}")
    public Ruc getRuc(@PathVariable String ruc) {
        return rucServicio.findByRuc(ruc);
    }

    /**
     * Metodo para crear un ruc
     * 
     * @param ruc
     * @return
     * 
     */
    @PostMapping("/")
    public Ruc postUserAgent(@Valid @RequestBody Ruc ruc) {
        return rucServicio.createRuc(ruc, this.getClass().getName());
    }

    /**
     * Metodo para borrar el ruc
     * 
     */
    @DeleteMapping("/{ruc}")
    public void deleteUserAgent(@PathVariable String ruc) {
        rucServicio.deleteRuc(ruc);
    }
}