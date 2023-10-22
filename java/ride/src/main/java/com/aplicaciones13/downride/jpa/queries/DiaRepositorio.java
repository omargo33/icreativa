package com.aplicaciones13.downride.jpa.queries;


import org.springframework.data.jpa.repository.JpaRepository;

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

}
