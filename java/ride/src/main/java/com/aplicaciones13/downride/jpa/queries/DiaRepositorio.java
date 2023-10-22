package com.aplicaciones13.downride.jpa.queries;


import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aplicaciones13.downride.jpa.model.Dia;


/**
 * Clase para el repositorio Dia
 * 
 * @author omargo33@gmail.com
 * @version 2023-10-21
 * 
 */
public interface DiaRepositorio extends JpaRepository<Dia, Integer> {

    /**
     * Metodo para buscar por id_dias
     */
    Dia findByIdDias(Integer idDias);

    /**
     * Metodo valida si existe DIA buscando por id_rucs y fecha
     * 
     * @param idRucs
     * @param dia
     * 
     */
    @Query(value = "SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM dias d WHERE d.id_rucs = ?1 AND d.dia = ?2", nativeQuery = true)
    boolean existsByIdRucsAndDiaConsultado(Integer idRucs, Date dia);

}
