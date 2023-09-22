package com.aplicaciones13.placas.servicio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aplicaciones13.placas.jpa.model.Parametro;
import com.aplicaciones13.placas.jpa.queries.ParametroRepositorio;

import lombok.RequiredArgsConstructor;

/**
 * Clase para el servicio de los parametros
 * 
 * @author omargo33@gmail.com
 * @version 2023-08-20
 *
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ParametroServicio {

    @Autowired
    private final ParametroRepositorio parametroRepositorio;

    /**
     * Metodo para buscar un id por parametro.
     * 
     * @param id
     */
    public Parametro findByIdParametros(Integer id) {
        return parametroRepositorio.findByIdParametros(id);
    }

    /**
     * Metodo para buscar varios id por parametro.
     * 
     * @param idParametros
     * @return
     */
    public Map<Integer,Parametro> findByIdParametrosIn(List<Integer> idParametros) {
        
        Map <Integer,Parametro> mapParametros = new HashMap<>();

        List<Parametro> listaParametros = parametroRepositorio.findByIdParametrosIn(idParametros);

        for (Parametro parametro : listaParametros) {
            mapParametros.put(parametro.getIdParametros(), parametro);
        }
        
        return mapParametros;
    }

    /**
     * Metodo para actualizar un parametro
     * 
     * @param valor
     */
    public Parametro updateParametro(Parametro valor) {
        Parametro parametro = parametroRepositorio.findByIdParametros(valor.getIdParametros());

        if (parametro == null) {
            return null;
        }

        return parametroRepositorio.save(valor);
    }

}
