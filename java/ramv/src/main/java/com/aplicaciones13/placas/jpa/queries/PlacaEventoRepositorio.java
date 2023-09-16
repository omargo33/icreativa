package com.aplicaciones13.placas.jpa.queries;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aplicaciones13.placas.jpa.model.PlacaEvento;


/**
 * Interface para el repositorio de PlacaEvento
 * 
 * Se utiliza para realizar las consultas a la base de datos
 * 
 * @author omargo33@gmail.com
 * @version 2021-08-20
 */
public interface PlacaEventoRepositorio extends JpaRepository<PlacaEvento, Integer> {

    /**
     * Metodo para buscar por la placa desde un query nativo con subconsulta a placa
     * 
     * @param placa
     * 
     */
    List<PlacaEvento> findByIdPlacas(Integer idPlacas);

    /**
     * Metodo para buscar el primer un evento por id y por estado en 'M'
     * 
     * @param idPlacas
     */
    @Query(value = "SELECT * FROM placas_eventos WHERE id_placas = ?1 AND estado = 'M' LIMIT 1", nativeQuery = true)
    PlacaEvento findByIdPlacasAndEstadoM(Integer idPlacas);
    
}
