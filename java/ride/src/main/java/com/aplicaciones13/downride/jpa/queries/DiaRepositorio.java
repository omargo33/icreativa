package com.aplicaciones13.downride.jpa.queries;

import java.util.List;

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
     * Metodo listar los dia por id_rucs en estado P. y ordenado por id_rucs
     *  
     */
    @Query(value = "SELECT d FROM Dia d WHERE d.estado = 'P' ORDER BY d.idRucs")
    List<Dia> listarDiasPorIdRucsEnEstadoPendiente();

}
