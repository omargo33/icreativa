package com.aplicaciones13.downride.http.request;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aplicaciones13.downride.http.request.body.DocumentoRangoRequiest;
import com.aplicaciones13.downride.http.request.body.DocumentoRequest;
import com.aplicaciones13.downride.jpa.model.Documento;
import com.aplicaciones13.downride.servicio.DocumentoServicio;

import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.extern.slf4j.Slf4j;

/**
 * Clase para consumir el servicio de dias.
 * 
 * @author omargo33@hotmail.com
 * @since 2023-08-20
 */
@RestController
@Tag(name = "documentos", description = "Servicio consultar los documentos autorizados")
@RequestMapping(value = "/v1/documentos")
@Slf4j
public class Documentos extends ComonControlador {

    @Autowired
    DocumentoServicio documentoServicio;

    /**
     * Metodo para consultar los documentos autorizados por identificacion_receptor y fecha_autorizacion
     * 
     * @param documentoRequest
     * @return
     */
    @GetMapping("/")
    public List<Documento>getDocumentos( @RequestBody DocumentoRequest documentoRequest){
        log.error(documentoRequest.toString());
        
        return documentoServicio.findByIdentificacionReceptorAndFechaAutorizacion(
            documentoRequest.getRuc(), 
            documentoRequest.getFecha());
    }

    /**
     * Metodo para consultar los documentos autorizados por identificacion_receptor y fecha_autorizacion en un rango de fechas
     * 
     * @param documentoRequest
     * @return
     */
    @PostMapping("/rango")
    public List<Documento>getDocumentosRango( @RequestBody DocumentoRangoRequiest documentoRequest){
        log.error("data");
        log.error(documentoRequest.toString());
        
        return documentoServicio.findByIdentificacionReceptorAndFechaAutorizacionRango(
            documentoRequest.getRuc(), 
            documentoRequest.getFechaInicio(),
            documentoRequest.getFechaFin());
    }
}
