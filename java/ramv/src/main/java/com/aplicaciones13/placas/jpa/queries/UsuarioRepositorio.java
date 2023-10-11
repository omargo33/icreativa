package com.aplicaciones13.placas.jpa.queries;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aplicaciones13.placas.jpa.model.Usuario;
import java.util.Optional;
 
/**
 * Repositorio para la tabla usuario.
 * 
 * @author omargo33@gmail.com
 * @since 2023-10-08
 * 
 */
public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {

    /**
     * Metodo para buscar por el campo usuario con query nativo
     * 
     * @param usuario
     */
    Optional <Usuario> findByUsuario(String usuario);

    /**
     * Metodo para conocer si un usuario existe (boolean)
     * 
     */
    boolean existsByUsuario(String usuario);

}
