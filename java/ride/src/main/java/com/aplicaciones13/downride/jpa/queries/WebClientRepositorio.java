package com.aplicaciones13.downride.jpa.queries;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aplicaciones13.downride.jpa.model.WebClient;

/**
 * Repositorio para la tabla web_client
 * 
 * @author omargo33@hotmail.com
 * @since 2023-09-15
 * 
 */
public interface WebClientRepositorio extends JpaRepository<WebClient, Integer> {

    /**
     * Metodo para buscar el maximo id de la tabla.
     */
    WebClient findByIdWebClient(Integer id);
    

    /**
     * Metodo para buscar el maximo id de la tabla.
     */
    List<WebClient> findAll();
}
