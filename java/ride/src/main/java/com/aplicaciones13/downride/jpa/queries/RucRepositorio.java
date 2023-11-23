package com.aplicaciones13.downride.jpa.queries;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aplicaciones13.downride.jpa.model.Ruc;

/**
 * Clase para el repositorio RUC
 * 
 * @author omargo33@gmail.com
 * @version 2023-10-21
 * 
 */
public interface RucRepositorio extends JpaRepository<Ruc, Integer> {

    /**
     * Metodo para buscar todos los rucs, pero que estado sea A
     */
    @Query(value = "SELECT id_rucs, ruc, contrasena, estado, usuario_fecha, usuario_programa FROM public.rucs WHERE estado = 'A'", nativeQuery = true)
    List<Ruc> findAll();

    /**
     * Metodo para buscar por id_rucs
     */
    @Query(value = "SELECT id_rucs, ruc, contrasena, estado, usuario_fecha, usuario_programa FROM public.rucs WHERE id_rucs = ?1 AND estado = 'A'", nativeQuery = true)
    Ruc findByIdRucs(Integer idRucs);

    /**
     * Metodo para buscar por ruc
     */
    @Query(value = "SELECT id_rucs, ruc, contrasena, estado, usuario_fecha, usuario_programa FROM public.rucs WHERE ruc = ?1 AND estado = 'A'", nativeQuery = true)
    Ruc findByRuc(String ruc);

    /**
     * Metodo para buscar todos los rucs en estado P
     */
    @Query(value = "SELECT id_rucs, ruc, contrasena, estado, usuario_fecha, usuario_programa FROM public.rucs WHERE estado = 'P'", nativeQuery = true)
    List<Ruc> findAllPendientes();

    /*
     * Metodo para actualizar la contrasena con un metodo nativo de postgresql
     *
     * @param idRucs
     * @param constrasena
     */
    @Query(value = "UPDATE public.rucs SET contrasena = ?2 WHERE id_rucs = ?1", nativeQuery = true)
    void updateContrasena(Integer idRucs, String contrasena);
    
}