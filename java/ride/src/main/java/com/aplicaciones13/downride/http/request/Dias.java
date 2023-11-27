package com.aplicaciones13.downride.http.request;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aplicaciones13.downride.jpa.model.Dia;
import com.aplicaciones13.downride.servicio.DiaServicio;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;


/**
 * Clase para consumir el servicio de dias.
 * 
 * @author omargo33@hotmail.com
 * @since 2023-08-20
 */
@RestController
@Tag(name = "dias", description = "Servicio para ingresar los dias a procesar")
@RequestMapping(value = "/v1/dias")
@Slf4j
public class Dias extends ComonControlador {

    @Autowired
    DiaServicio diaServicio;

    /** 
     * Metodo para cargar el dia de consulta
     *  
     */
    @PutMapping("/")
    public Dia crearDia(@Valid @RequestBody Dia valor){
        log.info(valor.toString());
        return diaServicio.crearDia(valor);
    }
}
