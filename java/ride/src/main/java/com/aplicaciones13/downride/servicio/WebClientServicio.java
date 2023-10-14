package com.aplicaciones13.downride.servicio;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aplicaciones13.downride.jpa.model.WebClient;
import com.aplicaciones13.downride.jpa.queries.WebClientRepositorio;

import lombok.RequiredArgsConstructor;

/**
 * Clase para el servicio userAgent
 * 
 * @author omargo33@gmail.com
 * @version 2023-08-20
 *
 */
@Service
@Transactional
@RequiredArgsConstructor
public class WebClientServicio {
    
    @Autowired
    private final WebClientRepositorio webClientRepositorio;

    /**
     * Metodo para listar todos los web clients.
     */
    public List<WebClient> findAll() {
        return webClientRepositorio.findAll();
    }

    /**
     * Metodo para buscar un web client por id.
     * 
     * @param idWebClient
     * @return
     */
    public WebClient findByIdWebClient(int idWebClient) {
        return webClientRepositorio.findByIdWebClient(idWebClient);
    }

    public WebClient crearWebClient(WebClient webClient) {
        return webClientRepositorio.save(webClient);
    }

    /** 
     * Metodo para borrar un web client por id.
     */
    public void borrarWebClient(int idWebClient) {
        webClientRepositorio.deleteById(idWebClient);
    }
}
