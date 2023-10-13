package com.aplicaciones13.placas.http.request;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aplicaciones13.placas.servicio.PlacaServicio;

import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Tag(name = "identificacion_vehicular", description = "Servicio para agregar RAMV, y listar los estados de los mismos")
@RequestMapping("/identificacion_vehicular")
public class IdentificacionVehicularControlador extends ComonControlador{

    @Autowired
    PlacaServicio placaServicio;


    @GetMapping("/{ramv}")
    public ResponseEntity<?> getEstadoRamv(@PathVariable String ramv) {
        


        placaServicio.findByPlaca(ramv);


        return ResponseEntity.ok(placaServicio.findByPlaca(ramv));
    }
    
}
