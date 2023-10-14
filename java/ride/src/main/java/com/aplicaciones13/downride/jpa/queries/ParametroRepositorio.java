package com.aplicaciones13.downride.jpa.queries;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aplicaciones13.downride.jpa.model.Parametro;

public interface ParametroRepositorio extends JpaRepository<Parametro, Integer> {

    /**
     * Metodo para buscar por id_parametros
     */
    Parametro findByIdParametros(Integer idParametros);

    /**
     * Metodo para buscar por varios id_parametros al mismo tiempo
     */
    List<Parametro> findByIdParametrosIn(List<Integer> idParametros);
}
