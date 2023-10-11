package com.aplicaciones13.placas.jpa.queries;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aplicaciones13.placas.jpa.model.Correo;


public interface CorreoRepositorio extends JpaRepository<Correo, Integer> {

    /**
     * Metodo para buscar por el campo correo con query nativo
     */
    @Query(value = "SELECT id_correos, correo, usuario, usuario_fecha, usuario_programa FROM public.correos WHERE correo = :correo", nativeQuery = true)
    Correo findByCorreo(String correo);

    /**
     * Metodo para buscar por el campo usuario con query nativo
     */
    @Query(value = "SELECT id_correos, correo, usuario, usuario_fecha, usuario_programa FROM public.correos WHERE usuario = :usuario", nativeQuery = true)
    List<Correo> findByUsuario(String usuario);

}